package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.content.res.AssetManager;

import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    private Button BtnCameraView;
    private EditText EditOcrResult;
    private String datapath = ""; // 언어데이터가 있는 경로
    private final String[] mLanguageList = {"eng","kor"}; // 언어

    private int ACTIVITY_REQUEST_CODE = 1;

    static TessBaseAPI sTess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, LoadingActivity.class);    // 스플래시 화면
        startActivity(intent);

        com.google.android.material.bottomnavigation.BottomNavigationView bottomNavigationView = (com.google.android.material.bottomnavigation.BottomNavigationView) findViewById(R.id.navigationView);
        // BottomNavigationView 메뉴를 선택할 때마다 위치가 변하지 않도록
        BottomNavigationView.removeShiftMode(bottomNavigationView);
        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);



        /* 뷰 선언 및 초기화 */
        // findViewByld() :  activity_main.xml 레이아웃에 설정된 뷰들을 가져오는 메소드
        BtnCameraView = (Button) findViewById(R.id.btn_camera);
        EditOcrResult = (EditText) findViewById(R.id.edit_ocrresult);
        sTess = new TessBaseAPI();

        //언어파일 경로
        datapath = getFilesDir() + "/tesseract/";

        //트레이닝데이터가 카피되어 있는지 체크
        String lang = "";
        for (String Language : mLanguageList) {
            checkFile(new File(datapath + "tessdata/"), Language);
            lang += Language + "+";
        }
        sTess.init(datapath, lang);

        // 버튼 클릭 시
        BtnCameraView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Camera를 실행하는 intent 호출
                Intent runCamera = new Intent(MainActivity.this, CameraView.class);
                startActivityForResult(runCamera, ACTIVITY_REQUEST_CODE); // 호출한 Activity에서 결과 받기
            }
        });
    }



    //check file on the device
    private void checkFile(File dir, String Language) {
        //디렉토리가 없으면 디렉토리를 만들고 그후에 파일을 카피
        if (!dir.exists() && dir.mkdirs()) {
            copyFiles(Language);
        }
        //디렉토리가 있지만 파일이 없으면 파일카피 진행
        if (dir.exists()) {
            String datafilepath = datapath + "tessdata/" + Language + ".traineddata";
            File datafile = new File(datafilepath);
            if (!datafile.exists()) {
                copyFiles(Language);
            }
        }
    }

    //copy file to device
    private void copyFiles(String Language) {
        try {
            String filepath = datapath + "/tessdata/" + Language + ".traineddata";
            AssetManager assetManager = getAssets();
            InputStream instream = assetManager.open("tessdata/"+Language+".traineddata");
            OutputStream outstream = new FileOutputStream(filepath);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, read);
            }
            outstream.flush();
            outstream.close();
            instream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 호출된 CameraView 액티비티 종료시 onActivityResult() 메서드로 전달받음
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK)
        {
            if(requestCode== ACTIVITY_REQUEST_CODE)
            {
                // 받아온 OCR 결과 출력
                EditOcrResult.setText(data.getStringExtra("STRING_OCR_RESULT"));
            }
        }
    }
}