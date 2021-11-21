package com.example.election

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.StringBuilder


class MainActivity : AppCompatActivity() {
    private var franchiseA = 0
    private var franchiseB = 0
    private var registerNumberList: List<Long> = mutableListOf()
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferences = getSharedPreferences("Election Preference", MODE_PRIVATE)
        btnVote.setOnClickListener {
            val checkRegisterNumber = etReg.text.toString()
            val checkFranchiseARadioButton = rbMarvel
            val checkFranchiseBRadioButton = rbDC
            var toggle = 0
            if (checkRegisterNumber.isEmpty() and (checkFranchiseARadioButton.isChecked || checkFranchiseBRadioButton.isChecked)) {
                Toast.makeText(
                    applicationContext,
                    "PLEASE ENTER YOUR REGISTER NUMBER  ",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (checkRegisterNumber.isNotEmpty() and (!checkFranchiseARadioButton.isChecked and !checkFranchiseBRadioButton.isChecked)) {
                Toast.makeText(
                    applicationContext,
                    "SELECT THE FRANCHISE ",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (checkRegisterNumber.isNotEmpty() and (checkFranchiseARadioButton.isChecked || checkFranchiseBRadioButton.isChecked)) {
                val registerNumber = etReg.text.toString().toLong()

                if (registerNumberList.contains(registerNumber)) {
                    Toast.makeText(
                        this,
                        "$registerNumber is already voted",
                        Toast.LENGTH_LONG
                    ).show()
                    toggle = 0
                } else {
                    toggle++
                    registerNumberList = registerNumberList + registerNumber
                }
                Toast.makeText(
                    applicationContext,
                    "Thanks For Voting :)",
                    Toast.LENGTH_SHORT
                ).show()
                if (toggle >= 1) {
                    when (registerNumber) {
                        in 310615104001..310615104120 -> if (checkFranchiseARadioButton.isChecked) franchiseA++ else if (checkFranchiseBRadioButton.isChecked) franchiseB++
                        in 0..1 -> if (checkFranchiseARadioButton.isChecked) franchiseA += 10 else if (checkFranchiseBRadioButton.isChecked) franchiseB += 10
                        in 2..50 -> if (checkFranchiseARadioButton.isChecked) franchiseA += 5 else if (checkFranchiseBRadioButton.isChecked) franchiseB += 5
                        else -> Toast.makeText(
                            this,
                            "Wrong Register Number",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "PLEASE ENTER REGISTER NUMBER AND SELECT THE FRANCHISE ",
                    Toast.LENGTH_SHORT
                ).show()
            }
            val globalVariable: GlobalVariable = GlobalVariable.instance
            globalVariable.winner = when {
                franchiseA > franchiseB -> {
                    "Marvel is Winner"
                }
                franchiseA < franchiseB -> {
                    "DC Comics is Winner"
                }
                else -> {
                    "No Winner Draw!"
                }
            }
            globalVariable.voteDifference = if (franchiseA > franchiseB) {
                franchiseA - franchiseB
            } else {
                franchiseB - franchiseA
            }
            globalVariable.totalVote = franchiseA + franchiseB
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val aboutDialog = AlertDialog.Builder(this)
            .setTitle("Elections")
            .setMessage("This App developed by PP Corp")
            .create()
        when (item.itemId) {
            R.id.extAbout -> aboutDialog.show()
            R.id.ifRules -> Intent(this, RulesActivity::class.java).also {
                startActivity(it)
            }
            R.id.extResult -> Intent(this, LoginActivity::class.java).also {
                startActivity(it)
            }
        }
        return true
    }

    override fun onPause() {
        super.onPause()
        val globalVariable: GlobalVariable = GlobalVariable.instance
        val editor = sharedPreferences.edit()
        val stringBuilder = StringBuilder()
        for (s in registerNumberList) {
            stringBuilder.append(s)
            stringBuilder.append(",")
        }
        val pauseResultWinner = globalVariable.winner
        val pauseTotalVote = globalVariable.totalVote
        val pauseVoteDifference = globalVariable.voteDifference
        val pauseCompetitorA = franchiseA
        val pauseCompetitorB = franchiseB
        editor.apply {
            putString("resultOfWinner", pauseResultWinner)
            if (pauseTotalVote != null) {
                putInt("totalVote", pauseTotalVote)
            }
            if (pauseVoteDifference != null) {
                putInt("voteDifference", pauseVoteDifference)
            }
            putInt("competitorA", pauseCompetitorA)
            putInt("competitorB", pauseCompetitorB)
            putString("string", stringBuilder.toString())
            apply()
        }

    }

    override fun onResume() {
        super.onResume()
        val globalVariable: GlobalVariable = GlobalVariable.instance
        val resumeResultOfWinner = sharedPreferences.getString("resultOfWinner", "")
        val resumeTotalVotes = sharedPreferences.getInt("totalVote", 0)
        val resumeDifferenceOfVote = sharedPreferences.getInt("voteDifference", 0)
        val resumeCompetitorA = sharedPreferences.getInt("competitorA", 0)
        val resumeCompetitorB = sharedPreferences.getInt("competitorB", 0)
        val wordString = sharedPreferences.getString("string", "")
        val resumeList = mutableListOf<Long>()
        //mList.add(0, 0)
        if (wordString != null && wordString.isNotEmpty()) {
            resumeList.addAll(wordString.split(",").filter { it.isNotEmpty() }.map { it.toLong() })
        }
        println("mList:$resumeList")

        for (i in resumeList.indices) {
            registerNumberList = registerNumberList + resumeList[i]
            println(registerNumberList)
        }
        globalVariable.winner = resumeResultOfWinner.toString()
        globalVariable.totalVote = resumeTotalVotes
        globalVariable.voteDifference = resumeDifferenceOfVote
        franchiseA = resumeCompetitorA
        franchiseB = resumeCompetitorB
    }

}


class GlobalVariable {
    var winner: String? = null
    var totalVote: Int? = null
    var voteDifference: Int? = null

    companion object {
        val instance = GlobalVariable()
    }
}

