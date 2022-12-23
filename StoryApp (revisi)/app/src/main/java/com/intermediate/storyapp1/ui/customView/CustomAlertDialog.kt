package com.intermediate.storyapp1.ui.customView

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.intermediate.storyapp1.R

class CustomAlertDialog (context: Context, private val message: Int, private val image: Int): AlertDialog(context) {
    init {
        setCancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.error_layout)

        val messageView = findViewById<TextView>(R.id.tvError)
        messageView.text = context.getString(message)

        val imageView = findViewById<ImageView>(R.id.ivEmptyUser)
        imageView.setImageResource(image)

        val dismissButton = findViewById<Button>(R.id.dismissButton)
        dismissButton.setOnClickListener {
            dismiss()
        }
    }
}