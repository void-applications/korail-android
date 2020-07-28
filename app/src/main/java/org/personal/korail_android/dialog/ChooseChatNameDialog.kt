package org.personal.korail_android.dialog

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import org.personal.korail_android.R

class ChooseChatNameDialog : DialogFragment(), DialogInterface.OnClickListener {

    private val TAG = javaClass.name
    private val selectedStation by lazy { arguments!!.getString("station") }
    private lateinit var dialogListener: DialogListener
    private lateinit var chooseChatNameET: EditText

    // 확인, 취소 버튼 구분하기 위한 변수
    private val confirmID: Int by lazy { -1 }
    private val cancelID: Int by lazy { -2 }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_choose_chat_name, null)

        chooseChatNameET = dialogView.findViewById(R.id.chooseChatNameET)

        return AlertDialog.Builder(activity)
            .setTitle("사용자 이름 정하기")
            .setView(dialogView)
            .setPositiveButton("확인", this)
            .setNegativeButton("취소", this)
            .create()
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        when (which) {
            confirmID -> dialogListener.onChooseName(selectedStation!!, chooseChatNameET.text.toString())
            cancelID -> dismiss()
        }
    }

    // 다이얼로그에서 액티비티로 데이터 전송
    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {

            dialogListener = context as DialogListener

        } catch (e: ClassCastException) {

            e.printStackTrace()
            Log.i(TAG, "onAttach : 인터페이스 implement 안함")
        }
    }

    interface DialogListener {
        fun onChooseName(station: String, chosenName: String)
    }
}