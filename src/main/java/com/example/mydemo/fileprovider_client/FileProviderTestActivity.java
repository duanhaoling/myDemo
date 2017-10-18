package com.example.mydemo.fileprovider_client;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mydemo.R;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileProviderTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_provider_test);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment()).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        private Button btnRequestFile;
        private TextView tvFileContent, tvFileName;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_fileprovider, container,
                    false);
            initializeViews(rootView);
            return rootView;
        }

        private void initializeViews(View rootView) {
            btnRequestFile = (Button) rootView.findViewById(R.id.btnRequestFile);
            btnRequestFile.setOnClickListener(clicklistener);
            tvFileContent = (TextView) rootView.findViewById(R.id.tvFileContent);
            tvFileName = (TextView) rootView.findViewById(R.id.tvFileName);
        }

        private View.OnClickListener clicklistener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (v == btnRequestFile) {
                    requestFile();
                }
            }
        };

        private void requestFile() {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("text/plain");
            intent.addCategory("android.intent.category.OPENABLE");
            intent.addCategory("android.intent.category.DEFAULT");
            startActivityForResult(intent, 0);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode,
                                     Intent data) {
            // TODO Auto-generated method stub
            if (resultCode != RESULT_OK) {
                tvFileContent.setText("未成功获得文件");
                tvFileName.setText("未成功获得文件");
            } else {
                readFile(data.getData());
            }
        }

        private void readFile(Uri returnUri) {
            Context context = getActivity();
            ParcelFileDescriptor inputPFD;
            //获取文件句柄
            try {
                inputPFD = context.getContentResolver().openFileDescriptor(returnUri, "r");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                tvFileContent.setText("获取文件句柄失败");
                tvFileName.setText("获取文件句柄失败");
                return;
            }

            //获取文件名字和大小
            Cursor returnCursor =
                    context.getContentResolver().query(returnUri, null, null, null, null);
            /*
		     * Get the column indexes of the data in the Cursor,
		     * move to the first row in the Cursor, get the data,
		     * and display it.
		     */
            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
            returnCursor.moveToFirst();
            tvFileName.setText("文件名:" + returnCursor.getString(nameIndex) + ", 大小:" +
                    Long.toString(returnCursor.getLong(sizeIndex)) + " B");
            returnCursor.close();

            //读取文件内容
            String content = "";
            FileReader fr = null;
            char[] buffer = new char[1024];

            try {
                StringBuilder strBuilder = new StringBuilder();
                fr = new FileReader(inputPFD.getFileDescriptor());
                while (fr.read(buffer) != -1) {
                    strBuilder.append(buffer);
                }
                fr.close();
                content = strBuilder.toString();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }finally {
                try {
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (content.length() != 0) {
                tvFileContent.setText(content);
            } else {
                tvFileContent.setText("<内容空>");
            }

            try {
                inputPFD.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}

