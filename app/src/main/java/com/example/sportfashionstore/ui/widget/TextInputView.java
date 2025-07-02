package com.example.sportfashionstore.ui.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.constraintlayout.widget.ConstraintLayout;


import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.sportfashionstore.R;

public class TextInputView extends ConstraintLayout {

    private AppCompatTextView tvTitle;
    private AppCompatEditText edtContent;
    private AppCompatTextView tvError;
    private AppCompatImageView btnClear;

    private OnTextChangedListener onTextChangedListener;

    public TextInputView(Context context) {
        super(context);
        init(context, null);
    }

    public TextInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TextInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.layout_text_input, this, true);

        tvTitle = findViewById(R.id.tv_title);
        edtContent = findViewById(R.id.edt_content);
        tvError = findViewById(R.id.tv_error);
        btnClear = findViewById(R.id.btn_clear);

        // Khởi tạo trạng thái mặc định
        btnClear.setVisibility(GONE);
        tvError.setVisibility(GONE);

        // Xử lý sự kiện TextWatcher cho EditText
        edtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Hiển thị/ẩn button clear
                btnClear.setVisibility(s.length() > 0 ? VISIBLE : GONE);

                // Callback khi text thay đổi
                if (onTextChangedListener != null) {
                    onTextChangedListener.onTextChanged(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Xử lý sự kiện click button clear
        btnClear.setOnClickListener(v -> {
            edtContent.setText("");
            if (onTextChangedListener != null) {
                onTextChangedListener.onTextChanged("");
            }
        });

        // Áp dụng các thuộc tính từ XML
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextInputView);

            // Xử lý title
            boolean titleVisible = typedArray.getBoolean(R.styleable.TextInputView_titleVisible, true);
            String titleText = typedArray.getString(R.styleable.TextInputView_titleText);
            setTitleVisible(titleVisible);
            if (titleText != null) {
                setTitleText(titleText);
            }

            // Xử lý hint cho EditText
            String hintText = typedArray.getString(R.styleable.TextInputView_hintText);
            if (hintText != null) {
                setHintText(hintText);
            }

            // Xử lý input type
            int inputType = typedArray.getInt(R.styleable.TextInputView_inputType, InputType.TYPE_CLASS_TEXT);
            setInputType(inputType);

            // Xử lý error
            boolean errorVisible = typedArray.getBoolean(R.styleable.TextInputView_errorVisible, false);
            String errorText = typedArray.getString(R.styleable.TextInputView_errorText);
            setErrorVisible(errorVisible);
            if (errorText != null) {
                setErrorText(errorText);
            }

            // Xử lý text cho EditText
            String text = typedArray.getString(R.styleable.TextInputView_text);
            if (text != null) {
                setText(text);
            }

            typedArray.recycle();
        }
    }

    // Phương thức để set title visibility
    public void setTitleVisible(boolean visible) {
        tvTitle.setVisibility(visible ? VISIBLE : GONE);
    }

    // Phương thức để set title text
    public void setTitleText(String title) {
        tvTitle.setText(title);
    }

    // Phương thức để set hint cho EditText
    public void setHintText(String hint) {
        edtContent.setHint(hint);
    }

    // Phương thức để set input type cho EditText
    public void setInputType(int inputType) {
        edtContent.setInputType(inputType);
    }

    // Phương thức để set error visibility
    public void setErrorVisible(boolean visible) {
        tvError.setVisibility(visible ? VISIBLE : GONE);
    }

    // Phương thức để set error text
    public void setErrorText(String error) {
        tvError.setText(error);
    }

    // Phương thức để lấy text từ EditText
    public String getText() {
        return edtContent.getText().toString();
    }

    // Phương thức để set text cho EditText
    public void setText(String text) {
        edtContent.setText(text);
    }

    // Phương thức để set listener
    public void setOnTextChangedListener(OnTextChangedListener listener) {
        this.onTextChangedListener = listener;
    }

    // Interface callback
    public interface OnTextChangedListener {
        void onTextChanged(String text);
    }

    // Getter methods
    public AppCompatEditText getEditText() {
        return edtContent;
    }

    public AppCompatTextView getTitleView() {
        return tvTitle;
    }

    public AppCompatTextView getErrorView() {
        return tvError;
    }

    public AppCompatImageView getClearButton() {
        return btnClear;
    }
}
