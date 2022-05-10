package com.example.timer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private EditText hourEditText;              // 시 텍스트 입력 객체
    private EditText minuteEditText;            // 분 텍스트 입력 객체
    private EditText secondEditText;            // 초 텍스트 입력 객체

    private TextView hourTextView;              // 시 텍스트 출력 객체
    private TextView minuteTextView;            // 분 텍스트 출력 객체
    private TextView secondTextView;            // 초 텍스트 출력 객체
    
    private LinearLayout timerTextViewLayout;   // 타이머 텍스트 출력 레이아웃 객체
    private LinearLayout timerEditViewLayout;   // 타이머 텍스트 입력 레이아웃 객체

    private Button btnStart;                    // 시작버튼 객체
    private Button btnPause;                    // 중단버튼 객체
    private Button btnReset;                    // 리셋버튼 객체

    private TextWatcher hourTextWatcher;        // 숫자 입력 제한을 걸기 위한 시 텍스트 옵저빙 객체
    private TextWatcher minuteTextWatcher;      // 숫자 입력 제한을 걸기 위한 분 텍스트 옵저빙 객체
    private TextWatcher secondTextWatcher;      // 숫자 입력 제한을 걸기 위한 초 텍스트 옵저빙 객체

    private ProgressBar timerProgressBar;       // 타이머 프로그레스 바 객체

    private CountDownTimer countDownTimer;      // 카운트다운 타이머 객체

    private InputMethodManager imm;             // 키보드 숨기기 기능 위한 객체

    private boolean timerRunning;               // 타이머 상태
    private boolean timerPaused;                // 타이머 멈춤 상태
    private boolean firstTimeRunning;           // 처음 돌리는지 상태

    private long initialTime;                   // 타이머에 처음 넣은 시간
    private long time;                          // 타이머 세팅 시간
    private long currentTime;                   // 타이머 현재 남은 시간

    // 어플리케이션을 처음 실행할때 최소 1회만 실행되는 코드
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hourEditText = (EditText)findViewById(R.id.hourEditText);
        minuteEditText = (EditText)findViewById(R.id.minuteEditText);
        secondEditText = (EditText)findViewById(R.id.secondEditText);

        hourTextView = (TextView)findViewById(R.id.hourTextView);
        minuteTextView = (TextView)findViewById(R.id.minuteTextView);
        secondTextView = (TextView)findViewById(R.id.secondTextView);

        timerTextViewLayout = (LinearLayout)findViewById(R.id.timerTextViewLayout);
        timerEditViewLayout = (LinearLayout)findViewById(R.id.timerEditTextLayout);

        btnStart = (Button)findViewById(R.id.btnStart);
        btnPause = (Button)findViewById(R.id.btnPause);
        btnReset = (Button)findViewById(R.id.btnReset);

        timerProgressBar = (ProgressBar)findViewById(R.id.timerProgressBar);

        hourEditText.addTextChangedListener(
                hourTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                ;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                if (text.length() > 0) {
                    if (text.length() == 3) {
                        hourEditText.setText(text.substring(1));
                    }
                    else if (text.length() == 1) {
                        String tmpText = hourEditText.getText().toString();
                        hourEditText.setText("0" + tmpText);
                    }
                    else if (text.length() == 0) {
                        hourEditText.setText("00");
                    }
                    else {
                        ;
                    }


                    if (Long.parseLong(hourEditText.getText().toString()) >= 100) {
                        hourEditText.setText("99");
                    }
                }
                hourEditText.setSelection(hourEditText.length());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    hourEditText.setText("00");
                }
            }
        });

        minuteEditText.addTextChangedListener(minuteTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                ;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                if (text.length() > 0) {
                    if (text.length() == 3) {
                        minuteEditText.setText(text.substring(1));
                    }
                    else if (text.length() == 1) {
                        String tmpText = minuteEditText.getText().toString();
                        minuteEditText.setText("0" + tmpText);
                    }
                    else if (text.length() == 0) {
                        minuteEditText.setText("00");
                    }
                    else {
                        ;
                    }


                    if (Long.parseLong(minuteEditText.getText().toString()) >= 60) {
                        minuteEditText.setText("59");
                    }
                }
                minuteEditText.setSelection(minuteEditText.length());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    minuteEditText.setText("00");
                }
            }
        });

        secondEditText.addTextChangedListener(secondTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                ;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                if (text.length() > 0) {
                    if (text.length() == 3) {
                        secondEditText.setText(text.substring(1));
                    }
                    else if (text.length() == 1) {
                        String tmpText = secondEditText.getText().toString();
                        secondEditText.setText("0" + tmpText);
                    }
                    else if (text.length() == 0) {
                        secondEditText.setText("00");
                    }
                    else {
                        ;
                    }


                    if (Long.parseLong(secondEditText.getText().toString()) >= 60) {
                        secondEditText.setText("59");
                    }
                }
                secondEditText.setSelection(secondEditText.length());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    secondEditText.setText("00");
                }
            }
        });

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
                pauseTimerTask();
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
        imm.hideSoftInputFromWindow(hourEditText.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(minuteEditText.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(secondEditText.getWindowToken(), 0);
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
        return;
    }

    // 타이머 작동시 실행되는 함수
    // Start 버튼 클릭시 실행
    private void startTimerTask()
    {
        if (timerRunning == false) {
            if (firstTimeRunning == true) {
                Long hour = Long.parseLong(hourEditText.getText().toString());
                Long minute = Long.parseLong(minuteEditText.getText().toString());
                Long second = Long.parseLong(secondEditText.getText().toString());
                time = (hour * 3600 + minute * 60 + second * 1) * 1000;
                if (time == 0) {
                    return;
                }
                initialTime = time;
                updateTimeTextView(time);
            }
            else if (timerPaused == true) {
                Long hour = Long.parseLong(hourTextView.getText().toString());
                Long minute = Long.parseLong(minuteTextView.getText().toString());
                Long second = Long.parseLong(secondTextView.getText().toString());
                time = (hour * 3600 + minute * 60 + second * 1) * 1000;
                updateTimeTextView(time);
                timerPaused = false;
            }
            else {
                time = currentTime;
            }

            onOffTimerLayouts(false, true);

            countDownTimer = new CountDownTimer(time, 10) {
                @Override
                public void onTick(long l) {
                    currentTime = l;
                    updateTimeTextView(currentTime);
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

    // 타이머 레이아웃 온오프 함수
    private void onOffTimerLayouts(boolean onTimerEditTextLayout, boolean onTimerTextViewLayout)
    {
        if (onTimerEditTextLayout == true) {
            timerEditViewLayout.setVisibility(View.VISIBLE);
        }
        else {
            timerEditViewLayout.setVisibility(View.INVISIBLE);
        }

        if (onTimerTextViewLayout == true) {
            timerTextViewLayout.setVisibility(View.VISIBLE);
        }
        else {
            timerTextViewLayout.setVisibility(View.INVISIBLE);
        }
        return;
    }
    
    // 타이머 입력 텍스트 업데이트 함수
    private void updateTimeEditText(long t)
    {
        long hour = t / 3600000;
        long minute = (t % 3600000) / 60000;
        long second = (t % 3600000) % 60000 / 1000;

        // hourTextView 입력대기상태로 만들어서 setText로 텍스트내용 변환
        hourEditText.post(new Runnable() {
            @Override
            public void run() {
                if (hour <= 9 && hour >= 0) {
                    hourEditText.setText("0" + hour);
                }
                else {
                    hourEditText.setText(Long.toString(hour));
                }
            }
        });

        minuteTextView.post(new Runnable() {
            @Override
            public void run() {
                if (minute <= 9 && minute >= 0) {
                    minuteEditText.setText("0" + minute);
                }
                else {
                    minuteEditText.setText(Long.toString(minute));
                }
            }
        });

        secondEditText.post(new Runnable() {
            @Override
            public void run() {
                if (second <= 9 && second >= 0) {
                    secondEditText.setText("0" + second);
                }
                else {
                    secondEditText.setText(Long.toString(second));
                }
            }
        });
        return;
    }
    
    // 타이머 출력 텍스트 업데이트 함수
    private void updateTimeTextView(long t)
    {
        long hour = t / 3600000;
        long minute = (t % 3600000) / 60000;
        long second = (t % 3600000) % 60000 / 1000;

        // hourTextView 입력대기상태로 만들어서 setText로 텍스트내용 변환
        hourTextView.post(new Runnable() {
            @Override
            public void run() {
                if (hour <= 9 && hour >= 0) {
                    hourTextView.setText("0" + hour);
                }
                else {
                    hourTextView.setText(Long.toString(hour));
                }
            }
        });

        minuteTextView.post(new Runnable() {
            @Override
            public void run() {
                if (minute <= 9 && minute >= 0) {
                    minuteTextView.setText("0" + minute);
                }
                else {
                    minuteTextView.setText(Long.toString(minute));
                }
            }
        });

        secondTextView.post(new Runnable() {
            @Override
            public void run() {
                if (second <= 9 && second >= 0) {
                    secondTextView.setText("0" + second);
                }
                else {
                    secondTextView.setText(Long.toString(second));
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
            btnPause.setText("CONTINUE");
            timerPaused = true;
            timerRunning = false;
        }
        else {
            btnPause.setText("PAUSE");
            startTimerTask();
        }
        return;
    }

    // 타이머 리셋시 실행되는 함수
    // Reset 버튼 클릭시 실행
    private void resetTimerTask()
    {
        countDownTimer.cancel();
        updateTimeEditText(initialTime);
        onOffTimerLayouts(true, false);

        timerProgressBar.setProgress(1000);

        timerRunning = false;
        firstTimeRunning = true;

        hideButton(true, false, false);
        return;
    }
}