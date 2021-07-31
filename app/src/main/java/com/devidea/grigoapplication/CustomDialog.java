package com.devidea.grigoapplication;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.devidea.grigoapplication.LoginActivity.retrofitService;

public class CustomDialog extends Dialog {

    EditText editText;
    Button button;
    private OnDialogListener listener;

    public CustomDialog(@NonNull Context context, final Long commentID, String comment) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.custom_dialog);

        editText = findViewById(R.id.editTextText);
        editText.setText(comment);

        button = findViewById(R.id.button2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = editText.getText().toString();
                JsonObject json = new JsonObject();
                json.addProperty("content", comment);
                retrofitService.updateComment(commentID, json).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });

                dismiss();
            }
        });



    }
    //추후 리스너 이용해 수정 함수 bodyFragment 이동, 화면 업데이트
    public void setDialogListener(OnDialogListener listener){ this.listener = listener; }


}
