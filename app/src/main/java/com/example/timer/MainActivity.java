package com.example.timer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText hourEditText;              // 시 텍스트 입력 객체
    private EditText minuteEditText;            // 분 텍스트 입력 객체
    private EditText secondEditText;            // 초 텍스트 입력 객체

    private TextView hourTextView;              // 시 텍스트 출력 객체
    private TextView minuteTextView;            // 분 텍스트 출력 객체
    private TextView secondTextView;            // 초 텍스트 출력 객체
    
    private LinearLayout timerTextViewLayout;   // 타이머 텍스트 출력 레이아웃 객체
    private LinearLayout timerEditViewLayout;   // 타이머 텍스트 입력 레이아웃 객체
    private LinearLayout timerPresetLayout;     // 타이머 프리셋 레이아웃

    private Button startButton;                 // 시작버튼 객체
    private Button pauseButton;                 // 중단버튼 객체
    private Button resetButton;                 // 리셋버튼 객체

    private MenuItem normalModeMenuItem;        // 노말모드 타이머 메뉴 객체
    private MenuItem presetModeMenuItem;        // 프리셋모드 타이머 메뉴 객체
    private MenuItem loopOnceModeMenuItem;      // 한번 루프하는 메뉴 객체
    private MenuItem loopInfiniteModeMenuItem;  // 무한히 루프하는 메뉴 객체

    private RadioButton presetRadioButton1;     // 프리셋 저장 라디오 버튼 1 객체
    private RadioButton presetRadioButton2;     // 프리셋 저장 라디오 버튼 2 객체

    private ProgressBar timerProgressBar;       // 타이머 프로그레스 바 객체

    private CountDownTimer countDownTimer;      // 카운트다운 타이머 객체

    private InputMethodManager imm;             // 키보드 숨기기 기능 위한 객체

    private boolean isTimerRunning;             // 타이머 상태
    private boolean isTimerPaused;              // 타이머 멈춤 상태
    private boolean isFirstTimeRunning;         // 처음 돌리는지
    private boolean isTimerUsePreset;           // 타이머 프리셋 이용하는지
    private boolean isTimerLoop;                // 타이머 루프하는지
    private boolean isTimerLoopInfinite;        // 타이머 루프가 무한한지
    private boolean isFirstTimerStarting;       // 첫번째 타이머가 시작하는지
    private boolean isSecondTimerStarting;      // 두번째 타이머가 시작하는지

    /*
     * Normal :             isTimerUsePreset false  isTimerLoop false   isTimerLoopInfinite false
     * Separate :           isTimerUsePreset true   isTimerLoop false   isTimerLoopInfinite false
     * Loop (Once) :        isTimerUsePreset true   isTimerLoop true    isTimerLoopInfinite false
     * Loop (infinite) :    isTimerUsePreset true   isTimerLoop true    isTimerLoopInfinite true
     */

    private long initialTime;                   // 타이머에 처음 넣은 시간
    private long time;                          // 타이머 세팅 시간
    private long currentTime;                   // 타이머 현재 남은 시간

    // 어플리케이션을 처음 실행할때 최소 1회만 실행되는 코드
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Timer -Normal mode");

        hourEditText = (EditText)findViewById(R.id.hourEditText);
        minuteEditText = (EditText)findViewById(R.id.minuteEditText);
        secondEditText = (EditText)findViewById(R.id.secondEditText);

        hourTextView = (TextView)findViewById(R.id.hourTextView);
        minuteTextView = (TextView)findViewById(R.id.minuteTextView);
        secondTextView = (TextView)findViewById(R.id.secondTextView);

        timerTextViewLayout = (LinearLayout)findViewById(R.id.timerTextViewLayout);
        timerEditViewLayout = (LinearLayout)findViewById(R.id.timerEditTextLayout);
        timerPresetLayout = (LinearLayout)findViewById(R.id.timerPresetLayout);

        startButton = (Button)findViewById(R.id.startButton);
        pauseButton = (Button)findViewById(R.id.pauseButton);
        resetButton = (Button)findViewById(R.id.resetButton);

        normalModeMenuItem = (MenuItem)findViewById(R.id.timerModeNormal);
        presetModeMenuItem = (MenuItem)findViewById(R.id.timerModePreset);
        loopOnceModeMenuItem = (MenuItem)findViewById(R.id.timerModeLoopOnce);
        loopInfiniteModeMenuItem = (MenuItem)findViewById(R.id.timerModeLoopInfinite);

        presetRadioButton1 = (RadioButton)findViewById(R.id.presetRadioButton1);
        presetRadioButton2 = (RadioButton)findViewById(R.id.presetRadioButton2);

        timerProgressBar = (ProgressBar)findViewById(R.id.timerProgressBar);


        isTimerRunning = false;
        isTimerPaused = false;
        isFirstTimeRunning = true;
        isTimerUsePreset = false;
        isTimerLoop = false;
        isTimerLoopInfinite = false;

        isFirstTimerStarting = true;
        isSecondTimerStarting = false;


        hourEditText.addTextChangedListener(new TextWatcher() {
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

                if (isTimerUsePreset == true) {
                    String changedText = hourEditText.getText().toString();
                    if (presetRadioButton1.isChecked() == true) {
                        String[] timeArray = presetRadioButton1.getText().toString().split(":");
                        presetRadioButton1.setText(changedText + ":" + timeArray[1] + ":" + timeArray[2]);
                    }

                    if (presetRadioButton2.isChecked() == true) {
                        String[] timeArray = presetRadioButton2.getText().toString().split(":");
                        presetRadioButton2.setText(changedText + ":" + timeArray[1] + ":" + timeArray[2]);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    hourEditText.setText("00");
                }
            }
        });

        minuteEditText.addTextChangedListener(new TextWatcher() {
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

                if (isTimerUsePreset == true) {
                    String changedText = minuteEditText.getText().toString();
                    if (presetRadioButton1.isChecked() == true) {
                        String[] timeArray = presetRadioButton1.getText().toString().split(":");
                        presetRadioButton1.setText(timeArray[0] + ":" + changedText + ":" + timeArray[2]);
                    }

                    if (presetRadioButton2.isChecked() == true) {
                        String[] timeArray = presetRadioButton2.getText().toString().split(":");
                        presetRadioButton2.setText(timeArray[0] + ":" + changedText + ":" + timeArray[2]);
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

        secondEditText.addTextChangedListener(new TextWatcher() {
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

                if (isTimerUsePreset == true) {
                    String changedText = secondEditText.getText().toString();
                    if (presetRadioButton1.isChecked() == true) {
                        String[] timeArray = presetRadioButton1.getText().toString().split(":");
                        presetRadioButton1.setText(timeArray[0] + ":" + timeArray[1] + ":" + changedText);
                    }

                    if (presetRadioButton2.isChecked() == true) {
                        String[] timeArray = presetRadioButton2.getText().toString().split(":");
                        presetRadioButton2.setText(timeArray[0] + ":" + timeArray[1] + ":" + changedText);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    secondEditText.setText("00");
                }
            }
        });


        presetRadioButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTimerRunning == false && isTimerPaused == false) {
                    presetRadioButton1.setChecked(true);
                    presetRadioButton2.setChecked(false);
                    changeTimerToPreset(true, false);
                }
                else {
                    if (presetRadioButton2.isChecked() == true) {
                        presetRadioButton1.setChecked(false);
                    }
                }
            }
        });

        presetRadioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTimerRunning == false && isTimerPaused == false) {
                    presetRadioButton2.setChecked(true);
                    presetRadioButton1.setChecked(false);
                    changeTimerToPreset(false, true);
                }
                else {
                    if (presetRadioButton1.isChecked() == true) {
                        presetRadioButton2.setChecked(false);
                    }
                }
            }
        });

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
            case R.id.startButton:
                if (isTimerLoop == false) {
                    startTimerTask();
                }
                else {
                    startTimerTaskLoopVer();
                }
                break;
            //멈춤
            case R.id.pauseButton:
                pauseTimerTask();
                break;
            // 리셋
            case R.id.resetButton:
                if (isTimerLoop == false) {
                    resetTimerTask();
                }
                else {
                    resetTimerTaskLoopVer();
                }
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
            startButton.setEnabled(true);
            startButton.setVisibility(View.VISIBLE);
        }
        else {
            startButton.setEnabled(false);
            startButton.setVisibility(View.INVISIBLE);
        }

        if (hidePauseButton == true) {
            pauseButton.setEnabled(true);
            pauseButton.setVisibility(View.VISIBLE);
        }
        else {
            pauseButton.setEnabled(false);
            pauseButton.setVisibility(View.INVISIBLE);
        }

        if (hideResetButton == true) {
            resetButton.setEnabled(true);
            resetButton.setVisibility(View.VISIBLE);
        }
        else {
            resetButton.setEnabled(false);
            resetButton.setVisibility(View.INVISIBLE);
        }
        return;
    }

    // 타이머 작동시 실행되는 함수
    // Start 버튼 클릭시 실행
    private void startTimerTask()
    {
        if (isTimerRunning == false) {
            if (isFirstTimeRunning == true) {
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
            else if (isTimerPaused == true) {
                Long hour = Long.parseLong(hourTextView.getText().toString());
                Long minute = Long.parseLong(minuteTextView.getText().toString());
                Long second = Long.parseLong(secondTextView.getText().toString());
                time = (hour * 3600 + minute * 60 + second * 1) * 1000;
                updateTimeTextView(time);
                isTimerPaused = false;
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

            isTimerRunning = true;
            isFirstTimeRunning = false;
        }
        return;
    }

    // 루프시 타이머 실행 함수
    private void startTimerTaskLoopVer()
    {
        if (isTimerRunning == false) {
            if (isFirstTimerStarting == true) {
                if (isFirstTimeRunning == true) {
                    presetRadioButton1.setChecked(true);
                    presetRadioButton2.setChecked(false);
                    changeTimerToPreset(true, false);

                    Long hour = Long.parseLong(hourEditText.getText().toString());
                    Long minute = Long.parseLong(minuteEditText.getText().toString());
                    Long second = Long.parseLong(secondEditText.getText().toString());
                    time = (hour * 3600 + minute * 60 + second * 1) * 1000;
                    if (time == 0) {
                        return;
                    }
                    initialTime = time;
                    updateTimeTextView(time);
                } else if (isTimerPaused == true) {
                    Long hour = Long.parseLong(hourTextView.getText().toString());
                    Long minute = Long.parseLong(minuteTextView.getText().toString());
                    Long second = Long.parseLong(secondTextView.getText().toString());
                    time = (hour * 3600 + minute * 60 + second * 1) * 1000;
                    updateTimeTextView(time);
                    isTimerPaused = false;
                } else {
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
                        countDownTimer.cancel();

                        isFirstTimerStarting = false;
                        isSecondTimerStarting = true;

                        timerProgressBar.setProgress(1000);
                        isFirstTimeRunning = true;
                        isTimerRunning = false;

                        startTimerTaskLoopVer();
                    }
                }.start();
            }
            if (isSecondTimerStarting == true) {
                if (isFirstTimeRunning == true) {
                    presetRadioButton1.setChecked(false);
                    presetRadioButton2.setChecked(true);
                    changeTimerToPreset(false, true);

                    Long hour = Long.parseLong(hourEditText.getText().toString());
                    Long minute = Long.parseLong(minuteEditText.getText().toString());
                    Long second = Long.parseLong(secondEditText.getText().toString());
                    time = (hour * 3600 + minute * 60 + second * 1) * 1000;
                    if (time == 0) {
                        resetTimerTaskLoopVer();
                        if (isTimerLoopInfinite == true) {
                            startTimerTaskLoopVer();
                        }
                        return;
                    }
                    initialTime = time;
                    updateTimeTextView(time);
                } else if (isTimerPaused == true) {
                    Long hour = Long.parseLong(hourTextView.getText().toString());
                    Long minute = Long.parseLong(minuteTextView.getText().toString());
                    Long second = Long.parseLong(secondTextView.getText().toString());
                    time = (hour * 3600 + minute * 60 + second * 1) * 1000;
                    updateTimeTextView(time);
                    isTimerPaused = false;
                } else {
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
                        resetTimerTaskLoopVer();
                        if (isTimerLoopInfinite == true) {
                            startTimerTaskLoopVer();
                        }
                    }
                }.start();
            }



            hideButton(false, true, true);

            isTimerRunning = true;
            isFirstTimeRunning = false;
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
        if (isTimerRunning != false && isTimerPaused == false) {
            if (currentTime < 1000) {
                return;
            }
            countDownTimer.cancel();
            pauseButton.setText("CONTINUE");
            isTimerPaused = true;
            isTimerRunning = false;
        }
        else {
            pauseButton.setText("PAUSE");
            if (isTimerLoop == false) {
                startTimerTask();
            }
            else {
                startTimerTaskLoopVer();
            }
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

        isTimerRunning = false;
        isFirstTimeRunning = true;

        hideButton(true, false, false);
        return;
    }

    // 루프시 타이머 리셋 함수
    private void resetTimerTaskLoopVer()
    {
        countDownTimer.cancel();
        presetRadioButton1.setChecked(true);
        presetRadioButton2.setChecked(false);
        changeTimerToPreset(true, false);
        isFirstTimerStarting = true;
        isSecondTimerStarting = false;
        onOffTimerLayouts(true, false);

        timerProgressBar.setProgress(1000);

        isTimerRunning = false;
        isFirstTimeRunning = true;

        hideButton(true, false, false);
        return;
    }

    // 프리셋 타이머로 옮기는 함수
    private void changeTimerToPreset(boolean isPreset1 , boolean isPreset2) {
        if (isTimerUsePreset == true) {
            if (isPreset1 == true) {
                String[] timeArray = presetRadioButton1.getText().toString().split(":");
                hourEditText.setText(timeArray[0]);
                minuteEditText.setText(timeArray[1]);
                secondEditText.setText(timeArray[2]);
            }
            if (isPreset2 == true) {
                String[] timeArray = presetRadioButton2.getText().toString().split(":");
                hourEditText.setText(timeArray[0]);
                minuteEditText.setText(timeArray[1]);
                secondEditText.setText(timeArray[2]);
            }
        }
    }


    // 메뉴
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // 메뉴선택 시 모드 변경 작업
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.timerModeNormal:
                if (isTimerRunning == true) {
                    resetTimerTask();
                }
                isTimerUsePreset = false;
                isTimerLoop = false;
                isTimerLoopInfinite = false;
                timerPresetLayout.setVisibility(View.GONE);
                updateTimeEditText(0);
                updateTimeTextView(0);
                setTitle("Timer -Normal mode");
                break;
            case R.id.timerModePreset:
                if (isTimerRunning == true) {
                    resetTimerTask();
                }
                isTimerUsePreset = true;
                isTimerLoop = false;
                isTimerLoopInfinite = false;
                timerPresetLayout.setVisibility(View.VISIBLE);
                updateTimeEditText(0);
                updateTimeTextView(0);
                presetRadioButton1.setText("00:00:00");
                presetRadioButton2.setText("00:00:00");
                setTitle("Timer -Preset mode");
                break;
            case R.id.timerModeLoopOnce:
                if (isTimerRunning == true) {
                    resetTimerTaskLoopVer();
                }
                isTimerUsePreset = true;
                isTimerLoop = true;
                isTimerLoopInfinite = false;
                isFirstTimerStarting = true;
                isSecondTimerStarting = false;
                timerPresetLayout.setVisibility(View.VISIBLE);
                updateTimeEditText(0);
                updateTimeTextView(0);
                presetRadioButton1.setText("00:00:00");
                presetRadioButton2.setText("00:00:00");
                setTitle("Timer -Loop(Once) mode");
                break;
            case R.id.timerModeLoopInfinite:
                if (isTimerRunning == true) {
                    resetTimerTaskLoopVer();
                }
                isTimerUsePreset = true;
                isTimerLoop = true;
                isTimerLoopInfinite = true;
                isFirstTimerStarting = true;
                isSecondTimerStarting = false;
                timerPresetLayout.setVisibility(View.VISIBLE);
                updateTimeEditText(0);
                updateTimeTextView(0);
                presetRadioButton1.setText("00:00:00");
                presetRadioButton2.setText("00:00:00");
                setTitle("Timer -Loop(Infinite) mode");
                break;
            default:
                break;
        }
        return true;
    }
}