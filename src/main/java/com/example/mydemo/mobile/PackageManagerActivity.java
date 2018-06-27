package com.example.mydemo.mobile;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mydemo.R;

import java.util.List;

/**
 * PackageManager //在安卓系统中负责管理所有已经安装的APP
 * ApplicationInfo //一种bean，用来存储应用程序的信息
 * <p>
 * 还有就是ApplicationInfo类里面有个标志属性，可以根据它跟一些内置的常量作位运算来判断
 * <p>
 * app.flags & ApplicationInfo.FLAG_SYSTEM == 1 //系统应用，反之是第三方
 * app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP == 1 //通过升级，从系统应用变成第三方应用
 * app.flags & ApplicationInfo.) == 1 //安装在外置SD卡，反之安装在内置SD卡
 */
public class PackageManagerActivity extends AppCompatActivity {
    List<ApplicationInfo> listApplications;
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_manager);
        mListView = findViewById(R.id.mListView);
        listApplications = getPackageManager().getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        mListView.setAdapter(new PMAdapter());
    }

    class PMAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return listApplications.size();
        }

        @Override
        public Object getItem(int position) {
            return listApplications.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_package, parent, false);
                holder = new ViewHolder();
                holder.icon = convertView.findViewById(R.id.icon);
                holder.logo = convertView.findViewById(R.id.logo);
                holder.packageName = convertView.findViewById(R.id.packageName);
                holder.labelName = convertView.findViewById(R.id.labelName);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.icon.setImageDrawable(listApplications.get(position).loadIcon(getPackageManager()));
            holder.logo.setImageDrawable(listApplications.get(position).loadLogo(getPackageManager()));
            holder.packageName.setText(listApplications.get(position).packageName);

            String source = "";
            if ((listApplications.get(position).flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                source = "系统";
            } else {
                source = "第三方";
            }

            if ((listApplications.get(position).flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 1) {
                source = source + ",Upldate->No-System";
            }

            if ((listApplications.get(position).flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == 1) {
                source = source + ",In External SD";
            } else {
                source = source + ",In Internal SD";
            }

            holder.labelName
                    .setText(listApplications.get(position).loadLabel(getPackageManager()) + "(" + source + ")");

            Log.i("INFOTAG", (String) listApplications.get(position).loadLabel(getPackageManager()));
            Log.i("INFOTAG", "-----backupAgentName:" + listApplications.get(position).backupAgentName);
            Log.i("INFOTAG", "-----compatibleWidthLimitDp:" + listApplications.get(position).compatibleWidthLimitDp);
            Log.i("INFOTAG", "-----className:" + listApplications.get(position).className);
            Log.i("INFOTAG", "-----dataDir:" + listApplications.get(position).dataDir);
            Log.i("INFOTAG", "-----descriptionRes:" + listApplications.get(position).descriptionRes);
            Log.i("INFOTAG", "-----flags:" + listApplications.get(position).flags);
            Log.i("INFOTAG", "-----labelRes:" + listApplications.get(position).labelRes);
            Log.i("INFOTAG", "-----largestWidthLimitDp:" + listApplications.get(position).largestWidthLimitDp);
            Log.i("INFOTAG", "-----manageSpaceActivityName:" + listApplications.get(position).manageSpaceActivityName);
            Log.i("INFOTAG", "-----name:" + listApplications.get(position).name);
            Log.i("INFOTAG", "-----nativeLibraryDir:" + listApplications.get(position).nativeLibraryDir);
            Log.i("INFOTAG", "-----permission:" + listApplications.get(position).permission);
            Log.i("INFOTAG", "-----processName:" + listApplications.get(position).processName);
            Log.i("INFOTAG", "-----publicSourceDir:" + listApplications.get(position).publicSourceDir);
            Log.i("INFOTAG", "-----requiresSmallestWidthDp:" + listApplications.get(position).requiresSmallestWidthDp);
            Log.i("INFOTAG", "-----sourceDir:" + listApplications.get(position).sourceDir);
            Log.i("INFOTAG", "-----targetSdkVersion:" + listApplications.get(position).targetSdkVersion);
            Log.i("INFOTAG", "-----taskAffinity:" + listApplications.get(position).taskAffinity);
            Log.i("INFOTAG", "-----uid:" + listApplications.get(position).uid);
            Log.i("INFOTAG", "-----theme:" + listApplications.get(position).theme);
            Log.i("INFOTAG", "-----uiOptions:" + listApplications.get(position).uiOptions);
            Log.i("INFOTAG", "");
            return convertView;
        }

        class ViewHolder {
            ImageView logo;
            ImageView icon;
            TextView packageName;
            TextView labelName;
        }

    }

}

