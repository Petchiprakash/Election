package com.example.election

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {
    //private var userInfo: UserInfo? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        val globalVariable : GlobalVariable = GlobalVariable.instance
        tvWinIS.text = globalVariable.winner
        tvVoteCountNumber.text = globalVariable.totalVote.toString()
        tvWon.text = globalVariable.voteDifference.toString()
    }
}