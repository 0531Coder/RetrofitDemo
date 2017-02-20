package com.example.moutain.retrofitdemo.module.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moutain.retrofitdemo.R;

/**
 * Created by MOUTAIN on 2016/10/31.
 */

public class InputActivity extends Activity implements View.OnClickListener {

    private TextView currentNum;
    private TextView tv_title;
    private EditText et_content;
    private TextView totalNum;
    private TextView txt_right;
    private ImageView del_number;
    private int textLimit;
    private int requestCode;
    private String content;
    private boolean isnull;
    private int type;
    private String title;
    private String hint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        initView();
        handleData();
    }

    private void handleData() {
        Intent intent = getIntent();
        //标题
        title = intent.getStringExtra("title");
        //输入的内容
        content = intent.getStringExtra("content");
        //请求码
        requestCode = intent.getIntExtra("requestCode", 0);
        //输入的数据的字数限制
        textLimit = intent.getIntExtra("textLimit", 0);
        //输入的数据是否可为空
        isnull = intent.getBooleanExtra("isNull", false);
        //输入数据的类型: 0为汉字，1为数字,2为身份证号
        type = intent.getIntExtra("type", 0);
        //显示的hint
        hint = intent.getStringExtra("hint");
        //回显内容
        if (!TextUtils.isEmpty(content)) {
            et_content.setText(content);
            Selection.setSelection(et_content.getText(), content.length());
        }
        //显示hint
        if(!TextUtils.isEmpty(hint)){
           et_content.setHint(hint);
        }
        if (!TextUtils.isEmpty(title)) {
            tv_title.setText(title);
        }
        //字数统计
        if (0 == textLimit) {
            currentNum.setVisibility(View.GONE);
            totalNum.setVisibility(View.GONE);
        } else {
            totalNum.setText("/" + textLimit);
        }
        if (1 == type) {
            //输入类型为数字
            et_content.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else if (2 == type) {
            //输入类型为数字和字母混排(身份证)，但是优先弹出数字提示框
            String digits = "1234567890xX";
            et_content.setKeyListener(DigitsKeyListener.getInstance(digits));
        }
        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_content.getText().length() > 0) {
                    del_number.setVisibility(View.VISIBLE);
                } else {
                    del_number.setVisibility(View.GONE);
                }

                int len = et_content.getText().length();
                if (textLimit > 0 && len > textLimit) {
                    currentNum.setText(et_content.getText().length());
                    Editable editable = et_content.getText();
                    int selEndIndex = Selection.getSelectionEnd(editable);
                    String str = editable.toString();
                    //截取新字符串
                    String newStr = str.substring(0, textLimit);
                    et_content.setText(newStr);
                    editable = et_content.getText();
                    //新字符串的长度
                    int newLen = editable.length();
                    //旧光标位置超过字符串长度
                    if (selEndIndex > newLen) {
                        selEndIndex = editable.length();
                    }
                    //设置新光标所在的位置
                    Selection.setSelection(editable, selEndIndex);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initView() {
        currentNum = (TextView) findViewById(R.id.currentNum);
        tv_title = (TextView) findViewById(R.id.tv_title);
        et_content = (EditText) findViewById(R.id.et_content);
        totalNum = (TextView) findViewById(R.id.totalNum);
        txt_right = (TextView) findViewById(R.id.txt_right);
        del_number = (ImageView) findViewById(R.id.del_number);
        txt_right.setOnClickListener(this);
        del_number.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_back:
                setResult(0);
                finish();
                break;
            case R.id.txt_right:
                if (!isnull) {
                    String content = et_content.getText().toString();
                    if (TextUtils.isEmpty(content)) {
                        Toast.makeText(InputActivity.this, title + "不能为空", Toast.LENGTH_SHORT);
                        return;
                    }
                }
                Intent intent = new Intent();
                intent.putExtra("content", et_content.getText());
                setResult(requestCode, intent);
                finish();
                break;
            case R.id.del_number:
                et_content.setText("");
                break;
        }

    }
}
