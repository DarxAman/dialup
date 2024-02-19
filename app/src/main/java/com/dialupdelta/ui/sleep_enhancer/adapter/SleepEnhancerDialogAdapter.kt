package com.dialupdelta.ui.sleep_enhancer.adapter

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dialupdelta.*
import com.dialupdelta.`interface`.AudioClickListener
import com.dialupdelta.data.network.response.sleep_enhancer_list_response.AudioFileList
import com.dialupdelta.data.network.response.sleep_enhancer_list_response.SleepEnhancerItem
import com.dialupdelta.utils.CustomProgressDialog


class SleepEnhancerDialogAdapter(private val context: Context, private val audioClickListener: AudioClickListener, private val programList:ArrayList<SleepEnhancerItem>?,
                                 val mediaPlayer: MediaPlayer
): RecyclerView.Adapter<SleepEnhancerDialogAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout =LayoutInflater.from(parent.context).inflate(R.layout.item_sleep_enhancer_dialog, parent, false)
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val program = programList?.get(position)
        holder.apply {
            programItemText.text = "A"
        }

        holder.sampleMp3.setOnClickListener {
            val songUrl = "https://app.dialupdelta.com/uploads/mp3/"+program?.file_name

            val customDialog = CustomProgressDialog(context)

            val  uriAudioUrl = Uri.parse(songUrl)
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
            try {
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.stop()
                    mediaPlayer.reset()
//                mediaPlayer.release()
                    mediaPlayer.setDataSource(context, uriAudioUrl)
                    mediaPlayer.prepareAsync()
                    mediaPlayer.setOnPreparedListener {
                        it.start()
                        customDialog.dismissSweet()
                    }
                }else {
                    mediaPlayer.setDataSource(context, uriAudioUrl)
                    mediaPlayer.prepareAsync()
                    mediaPlayer.setOnPreparedListener {
                        it.start()
                        customDialog.dismissSweet()
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

//        Glide.with(context).load("http://app.dialupdelta.com/uploads/thumb/"+program?.image).into(holder.imageMp3)
        Glide.with(context).load("http://app.dialupdelta.com/uploads/thumb/"+program?.graph_image).into(holder.graphSleep)

        holder.textParaSleep.text = program?.description
    }

    override fun getItemCount(): Int {
        return programList?.size?:0
    }

    inner class ViewHolder(view: View) :RecyclerView.ViewHolder(view){
        val programItemText = view.findViewById(R.id.programItemText) as TextView
        val textParaSleep = view.findViewById(R.id.textParaSleep) as TextView
        private val parentLayout = view.findViewById(R.id.parentLayout) as ConstraintLayout
        private val chooseAudio = view.findViewById(R.id.chooseAudio) as Button
        val sampleMp3 = view.findViewById(R.id.sampleMp3) as Button
        val graphSleep = view.findViewById(R.id.graphSleep) as ImageView

        init {
            parentLayout.setOnClickListener {
               audioClickListener.setOnAudioClickListener(bindingAdapterPosition)
            }

            chooseAudio.setOnClickListener {
                Toast.makeText(context, ""+bindingAdapterPosition, Toast.LENGTH_SHORT).show()
                audioClickListener.setOnAudioLongClickListener(bindingAdapterPosition)
                true
            }
        }
    }
}