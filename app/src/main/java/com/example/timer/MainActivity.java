package com.example.timer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private EditText editHourText;          // 시 텍스트 객체
    private EditText editMinuteText;        // 분 텍스트 객체
    private EditText editSecondText;        // 초 텍스트 객체

    private Button btnStart;                // 시작버튼 객체
    private Button btnPause;                // 중단버튼 객체
    private Button btnReset;                // 리셋버튼 객체
    
    private ProgressBar timerProgressBar;   // 타이머 프로그레스 바 객체

    private CountDownTimer countDownTimer;  // 카운트다운 타이머 객체

    private InputMethodManager imm;         // 키보드 숨기기 기능 위한 객체

    private boolean timerRunning;           // 타이머 상태
    private boolean timerPaused;            // 타이머 멈춤 상태
    private boolean firstTimeRunning;       // 처음 돌리는지 상태

    private long initialTime;               // 타이머에 처음 넣은 시간
    private long time;                      // 타이머 세팅 시간
    private long currentTime;               // 타이머 현재 남은 시간

    // 어플리케이션을 처음 실행할때 최소 1회만 실행되는 코드
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editHourText = (EditText)findViewById(R.id.hourEditText);
        editMinuteText = (EditText)findViewById(R.id.minuteEditText);
        editSecondText = (EditText)findViewById(R.id.secondEditText);

        btnStart = (Button)findViewById(R.id.btnStart);
        btnPause = (Button)findViewById(R.id.btnPause);
        btnReset = (Button)findViewById(R.id.btnReset);

        timerProgressBar = (ProgressBar)findViewById(R.id.timerProgressBar);

        timerRunning = false;
        timerPaused = false;
        firstTimeRunning = true;
        return;
    }

    // 어플리케이션 종료
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        return;
    }


    // 버튼 클릭 시 작동되는 함수
    public void clickHandler(View view)
    {
        hideKeyboard();
        switch(view.getId())
        {
            // 시작
            case R.id.btnStart:
                startTimerTask();
                break;
            //멈춤
            case R.id.btnPause:
                if (timerRunning == true) {
                    pauseTimerTask();
                }
                else {
                    startTimerTask();
                }
                break;
            // 리셋
            case R.id.btnReset :
                resetTimerTask();
                break;
        }
        return;
    }

    // 키보드 숨기기
    private void hideKeyboard()
    {
        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editHourText.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(editMinuteText.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(editSecondText.getWindowToken(), 0);
        return;
    }

    // start, pause, reset 버튼 숨기기
    private void hideButton(boolean hideStartButton, boolean hidePauseButton, boolean hideResetButton)
    {
        if (hideStartButton == true) {
            btnStart.setEnabled(true);
            btnStart.setVisibility(View.VISIBLE);
        }
        else {
            btnStart.setEnabled(false);
            btnStart.setVisibility(View.INVISIBLE);
        }

        if (hidePauseButton == true) {
            btnPause.setEnabled(true);
            btnPause.setVisibility(View.VISIBLE);
        }
        else {
            btnPause.setEnabled(false);
            btnPause.setVisibility(View.INVISIBLE);
        }

        if (hideResetButton == true) {
            btnReset.setEnabled(true);
            btnReset.setVisibility(View.VISIBLE);
        }
        else {
            btnReset.setEnabled(false);
            btnReset.setVisibility(View.INVISIBLE);
        }
    }

    // 타이머 작동시 실행되는 함수
    // Start 버튼 클릭시 실행
    private void startTimerTask()
    {
        if (timerRunning == false) {
            if (firstTimeRunning == true || timerPaused == true) {
                Long hour = Long.parseLong(editHourText.getText().toString());
                Long minute = Long.parseLong(editMinuteText.getText().toString());
                Long second = Long.parseLong(editSecondText.getText().toString());
                time = (hour * 3600 + minute * 60 + second * 1) * 1000;
                if (time == 0) {
                    return;
                }
                if (firstTimeRunning == true) {
                    initialTime = time;
                }
                timerPaused = false;
            } else {
                time = currentTime;
            }

            countDownTimer = new CountDownTimer(time, 10) {
                @Override
                public void onTick(long l) {
                    currentTime = l;
                    updateTimeText(currentTime);
                    updateTimerProgressBar();
                }

                @Override
                public void onFinish() {
                    resetTimerTask();
                }
            }.start();

            hideButton(false, true, true);

            timerRunning = true;
            firstTimeRunning = false;
        }
        return;
    }

    // 타이머 업데이트 함수
    private void updateTimeText(long t)
    {
        long hour = t / 3600000;
        long minute = (t % 3600000) / 60000;
        long second = (t % 3600000) % 60000 / 1000;

        // editHourText 입력대기상태로 만들어서 setText로 텍스트내용 변환
        editHourText.post(new Runnable() {
            @Override
            public void run() {
                if (hour <= 9 && hour >= 0) {
                    editHourText.setText("0" + hour);
                }
                else {
                    editHourText.setText(Long.toString(hour));
                }
            }
        });

        editMinuteText.post(new Runnable() {
            @Override
            public void run() {
                if (minute <= 9 && minute >= 0) {
                    editMinuteText.setText("0" + minute);
                }
                else {
                    editMinuteText.setText(Long.toString(minute));
                }
            }
        });

        editSecondText.post(new Runnable() {
            @Override
            public void run() {
                if (second <= 9 && second >= 0) {
                    editSecondText.setText("0" + second);
                }
                else {
                    editSecondText.setText(Long.toString(second));
                }
            }
        });
        return;
    }

    // 타이머 프로그레스 바 업데이트 함수
    private void updateTimerProgressBar()
    {
        timerProgressBar.setProgress((int)((double)currentTime / initialTime * 1000));
        return;
    }

    // 타이머 퍼즈시 실행되는 함수
    // Pause 버튼 클릭시 실행
    private void pauseTimerTask()
    {
        if (timerRunning != false && timerPaused == false) {
            countDownTimer.cancel();
            timerPaused = true;
            timerRunning = false;
        }
        return;
    }

    // 타이머 리셋시 실행되는 함수
    // Reset 버튼 클릭시 실행
    private void resetTimerTask()
    {
        countDownTimer.cancel();
        updateTimeText(initialTime);

        timerProgressBar.setProgress(1000);

        timerRunning = false;
        firstTimeRunning = true;

        hideButton(true, false, false);
        return;
    }
}