package com.example.mydemo.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mydemo.R;
import com.ldh.androidlib.view.dialog.nice.BaseNiceDialog;
import com.ldh.androidlib.view.dialog.nice.DialogViewHolder;
import com.ldh.androidlib.view.dialog.nice.NiceDialog;
import com.ldh.androidlib.view.dialog.nice.ViewConvertListener;

public class DialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
    }

    public void showDialog0(View view) {
        NiceDialog.init()
                .setLayoutId(R.layout.share_layout)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(DialogViewHolder holder, final BaseNiceDialog dialog) {
                        holder.setOnClickListenerWithDismiss(R.id.wechat, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(DialogActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(DialogActivity.this, DialogActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                })
                .setDimAmount(0.3f)
                .setShowBottom(true)
                .show(getSupportFragmentManager());
    }

    public void showDialog1(View view) {
        NiceDialog.init()
                .setLayoutId(R.layout.friend_set_layout)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(DialogViewHolder holder, final BaseNiceDialog dialog) {

                    }
                })
                .setShowBottom(true)
                .setHeight(310)
                .show(getSupportFragmentManager());
    }

    public void showDialog2(View view) {
        NiceDialog.init()
                .setLayoutId(R.layout.commit_layout)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(DialogViewHolder holder, final BaseNiceDialog dialog) {
                        final EditText editText = holder.getView(R.id.edit_input);
                        editText.post(new Runnable() {
                            @Override
                            public void run() {
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.showSoftInput(editText, 0);
                            }
                        });
                    }
                })
                .setShowBottom(true)
                .show(getSupportFragmentManager());
    }

    public void showDialog3(View view) {
        NiceDialog.init()
                .setLayoutId(R.layout.ad_layout)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(DialogViewHolder holder, final BaseNiceDialog dialog) {
                        holder.setOnClickListenerWithDismiss(R.id.close, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }
                })
                .setWidth(210)
                .setOutCancel(false)
                .setAnimStyle(R.style.EnterExitAnimation)
                .show(getSupportFragmentManager());
    }


    public void showDialog4(View view) {
        NiceDialog.init()
                .setLayoutId(R.layout.loading_layout)
                .setWidth(100)
                .setHeight(100)
                .setDimAmount(0)
                .show(getSupportFragmentManager());
    }

    public void showDialog5(View view) {
        ConfirmDialog.newInstance("1")
                .setMargin(60)
                .setOutCancel(false)
                .show(getSupportFragmentManager());
    }

    public void showDialog6(View view) {
        ConfirmDialog.newInstance("2")
                .setMargin(60)
                .setOutCancel(false)
                .show(getSupportFragmentManager());
    }

    public static class ConfirmDialog extends BaseNiceDialog {
        private String type;

        public static ConfirmDialog newInstance(String type) {
            Bundle bundle = new Bundle();
            bundle.putString("type", type);
            ConfirmDialog dialog = new ConfirmDialog();
            dialog.setArguments(bundle);
            return dialog;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Bundle bundle = getArguments();
            if (bundle == null) {
                return;
            }
            type = bundle.getString("type");
        }

        @Override
        public int initLayoutId() {
            return R.layout.confirm_layout;
        }

        @Override
        public void convertView(DialogViewHolder holder, final BaseNiceDialog dialog) {
            if ("1".equals(type)) {
                holder.setText(R.id.title, "提示");
                holder.setText(R.id.message, "您已支付成功！");
            } else if ("2".equals(type)) {
                holder.setText(R.id.title, "警告");
                holder.setText(R.id.message, "您的账号已被冻结！");
            }
            holder.setOnClickListenerWithDismiss(R.id.cancel, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            holder.setOnClickListenerWithDismiss(R.id.ok, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
    }
}
