package com.example.android.bleviewer

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.layout_details.view.*

class TextFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater!!.inflate(R.layout.layout_details, container, false)
        val s = arguments.getString("msg")

        v.editText_adv.text = s
        return v
    }

    companion object {
        fun newInstance(text: String): TextFragment {
            val f = TextFragment()

            val b = Bundle()
            b.putString("msg", text)
            f.arguments = b
            return f
        }
    }

    fun updateText(newText : String) {
        view!!.editText_adv.text = newText
    }
}