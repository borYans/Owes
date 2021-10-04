package com.example.owes.utils

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.Toast
import com.example.owes.R
import com.google.android.material.snackbar.Snackbar

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun View.classicSnackBar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
}

fun View.confirmationSnackBar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).apply {
        setBackgroundTint(resources.getColor(android.R.color.holo_green_dark))
        setTextColor(resources.getColor(R.color.white))
        setActionTextColor(resources.getColor(R.color.white))
    }.show()
}