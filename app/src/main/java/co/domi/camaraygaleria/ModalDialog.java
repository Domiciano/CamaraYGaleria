package co.domi.camaraygaleria;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ModalDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModalDialog extends DialogFragment implements View.OnClickListener{


    private EditText urlET;
    private Button okBtn;
    private OnOkListener listener;

    public ModalDialog() {
        // Required empty public constructor
    }


    public static ModalDialog newInstance() {
        ModalDialog fragment = new ModalDialog();
        Bundle args = new Bundle();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_modal_dialog, container, false);
        urlET = root.findViewById(R.id.urlET);
        okBtn = root.findViewById(R.id.okBtn);
        okBtn.setOnClickListener(this);

        return root;
    }

    public void setListener(OnOkListener listener){
        this.listener = listener;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.okBtn:
                if(listener == null){
                    Log.e("Error", "No hay observer");
                }else {
                    listener.onOk(urlET.getText().toString());
                }
                break;
        }

    }

    public interface OnOkListener{
        void onOk(String url);
    }


}