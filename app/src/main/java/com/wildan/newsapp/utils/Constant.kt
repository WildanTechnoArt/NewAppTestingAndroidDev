package com.wildan.newsapp.utils

import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.google.gson.Gson
import com.wildan.newsapp.R
import com.wildan.newsapp.model.Response
import com.wildan.newsapp.network.ConnectivityStatus
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object Constant {
    const val CONTENT = "content"

    fun handleErrorApi(context: FragmentActivity, it: Throwable) {
        if (ConnectivityStatus.isConnected(context)) {
            when (it) {
                is HttpException -> {
                    try {
                        val gson = Gson()
                        val response = gson.fromJson(
                            it.response()?.errorBody()?.charStream(),
                            Response::class.java
                        )
                        val message = response?.message.toString()
                        handleSessionAndException(it, context, message)
                    } catch (e: Exception) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.message_if_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                is SocketTimeoutException -> // connection errors
                    Toast.makeText(
                        context, "Connection Timeout!",
                        Toast.LENGTH_SHORT
                    ).show()

                else -> {
                    Toast.makeText(
                        context, it.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            Toast.makeText(
                context, context.getString(R.string.message_if_disconnect),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun formatPublishedDate(isoDate: String): String {
        val inputFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME
        val outputFormatter = DateTimeFormatter.ofPattern("EEE, d MMMM HH.mm", Locale("id", "ID"))

        val dateTime = ZonedDateTime.parse(isoDate, inputFormatter)
        return dateTime.format(outputFormatter)
    }

    fun <T, VH : RecyclerView.ViewHolder> handleData(
        page: Int?,
        takeItem: Boolean,
        data: List<T>?,
        adapter: ListAdapter<T, VH>,
        recyclerView: RecyclerView?,
        textView: MaterialTextView?
    ) {
        if (page != null) {
            val oldList = adapter.currentList
            if (page == 1) {
                adapter.submitList(data)
            } else {
                val newList = oldList.toMutableList().apply {
                    addAll(data ?: mutableListOf())
                }
                adapter.submitList(newList) {
                    recyclerView?.smoothScrollToPosition(adapter.itemCount - data?.size!!)
                }
            }
        } else {
            adapter.submitList(if (takeItem) data?.take(4) else data)
        }
        val isEmpty = data?.isEmpty() ?: false
        recyclerView?.visibility = if (isEmpty) View.GONE else View.VISIBLE
        textView?.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }


    private fun handleSessionAndException(
        t: HttpException,
        context: FragmentActivity,
        message: String?
    ) {
        if (t.code().toString() == "401") {
            Toast.makeText(
                context,
                "Maaf session anda telah berakhir",
                Toast.LENGTH_SHORT
            ).show()
            context.finishAffinity()
        } else {
            if (message != "null") {
                Toast.makeText(
                    context,
                    message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}