package com.devidea.grigoapplication;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.devidea.grigoapplication.LoginActivity.retrofitService;

public class CommentUpdateDialog extends Dialog {

    EditText editText;
    Button button;


    public CommentUpdateDialog(@NonNull Context context, final Long commentID, String comment) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_comment_update);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setGravity(Gravity.BOTTOM);

        getWindow().setAttributes((WindowManager.LayoutParams) params);

        editText = findViewById(R.id.editText);
        editText.setText(comment);

        button = findViewById(R.id.send);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = editText.getText().toString();
                JsonObject json = new JsonObject();
                json.addProperty("content", comment);
                retrofitService.updateComment(commentID, json).enqueue(new Callback<ResponseDTO>() {
                    @Override
                    public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {

                    }

                    @Override
                    public void onFailure(Call<ResponseDTO> call, Throwable t) {

                    }
                });

                dismiss();
            }
        });



    }

}
