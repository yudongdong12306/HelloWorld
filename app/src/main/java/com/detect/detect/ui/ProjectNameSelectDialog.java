package com.detect.detect.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.detect.detect.R;
import com.detect.detect.shared_preferences.ProjectDataManagerDB;
import com.detect.detect.shared_preferences.ProjectInfoSP;

import java.util.List;

/**
 * 固件更新需要点击确认的提示对话框
 * Created by dongdong.yu on 2017/9/15.
 */

public class ProjectNameSelectDialog extends DialogFragment {
    private static final String TAG = "ProjectNameSelectDialog";
    private ListView projectNameLv;
    private List<String> allProjectNames;
    private TextView projectNameCustomTv;
    private Button projectNameConfirmBt;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_project_name_fragment, container, false);
//        Bundle arguments = getArguments();
//        tips = arguments("project_names");
        initView(view);
        initClick();
        return view;
    }

    private void initClick() {
//        confirmTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Activity activity = getActivity();
//                if (activity != null && activity instanceof ConfirmClickCallback) {
//                    ConfirmClickCallback callback = (ConfirmClickCallback) activity;
//                    callback.onConfirm();
//                }
//                //点击确认后,弹框消失
//                dismiss();
//            }
//        });
        projectNameLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (allProjectNames == null) {
                    dismiss();
                    return;
                }
                String projectName = allProjectNames.get(position);
                Activity activity = getActivity();
                if (activity != null && activity instanceof ConfirmClickCallback) {
                    ConfirmClickCallback callback = (ConfirmClickCallback) activity;
                    callback.onConfirm(projectName);
                }
                dismiss();
            }
        });
        projectNameConfirmBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                if (activity != null && activity instanceof ConfirmClickCallback) {
                    ConfirmClickCallback callback = (ConfirmClickCallback) activity;
                    callback.onCustomProjectName(projectNameCustomTv.getText().toString().trim());
                }
                dismiss();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            Window window = dialog.getWindow();
            if (window != null) {
                window.setDimAmount(0.25f);
                window.setLayout((int) (dm.widthPixels * 0.6), (int) (dm.heightPixels * 0.7) /*ViewGroup.LayoutParams.WRAP_CONTENT*/);
            }
        }
    }


    private void initView(View view) {
        ProjectDataManagerDB instance = ProjectDataManagerDB.getInstance();
        allProjectNames = instance.getAllProjectNames();
        projectNameLv = view.findViewById(R.id.project_name_lv);
        projectNameCustomTv = view.findViewById(R.id.project_name_custom_tv);
        projectNameConfirmBt = view.findViewById(R.id.project_name_custom_bt);
        ProjectNamesAdapter adapter = new ProjectNamesAdapter(getActivity(), allProjectNames);
        projectNameLv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    interface ConfirmClickCallback {
        void onConfirm(String projectName);

        void onCustomProjectName(String trim);
    }
}
