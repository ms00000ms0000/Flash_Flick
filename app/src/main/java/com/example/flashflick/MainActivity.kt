package com.example.flashflick

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

data class Flashcard(var question: String, var answer: String)

class MainActivity : AppCompatActivity() {

    private val flashcards = mutableListOf(
        Flashcard("Capital of India?", "New Delhi"),
        Flashcard("5 + 3 =", "8"),
        Flashcard("Kotlin is developed by?", "JetBrains")
    )

    private var currentIndex = 0
    private var answerVisible = false

    private lateinit var questionTextView: TextView
    private lateinit var answerTextView: TextView
    private lateinit var showAnswerButton: Button
    private lateinit var nextButton: Button
    private lateinit var prevButton: Button
    private lateinit var addButton: Button
    private lateinit var editButton: Button
    private lateinit var deleteButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find all views
        questionTextView = findViewById(R.id.questionTextView)
        answerTextView = findViewById(R.id.answerTextView)
        showAnswerButton = findViewById(R.id.showAnswerButton)
        nextButton = findViewById(R.id.nextButton)
        prevButton = findViewById(R.id.prevButton)
        addButton = findViewById(R.id.addButton)
        editButton = findViewById(R.id.editButton)
        deleteButton = findViewById(R.id.deleteButton)

        updateCard()

        showAnswerButton.setOnClickListener {
            answerVisible = !answerVisible
            answerTextView.visibility = if (answerVisible) View.VISIBLE else View.GONE
        }

        nextButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % flashcards.size
            updateCard()
        }

        prevButton.setOnClickListener {
            currentIndex = if (currentIndex == 0) flashcards.size - 1 else currentIndex - 1
            updateCard()
        }

        addButton.setOnClickListener {
            showAddEditDialog(isEdit = false)
        }

        editButton.setOnClickListener {
            showAddEditDialog(isEdit = true)
        }

        deleteButton.setOnClickListener {
            if (flashcards.isNotEmpty()) {
                flashcards.removeAt(currentIndex)
                if (currentIndex >= flashcards.size) currentIndex = 0
                updateCard()
            }
        }
    }

    private fun updateCard() {
        if (flashcards.isEmpty()) {
            questionTextView.text = "No flashcards available"
            answerTextView.visibility = View.GONE
            return
        }
        val card = flashcards[currentIndex]
        questionTextView.text = card.question
        answerTextView.text = card.answer
        answerTextView.visibility = if (answerVisible) View.VISIBLE else View.GONE
    }

    private fun showAddEditDialog(isEdit: Boolean) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_edit_flashcard, null)
        val questionEdit = dialogView.findViewById<EditText>(R.id.editQuestion)
        val answerEdit = dialogView.findViewById<EditText>(R.id.editAnswer)

        if (isEdit) {
            questionEdit.setText(flashcards[currentIndex].question)
            answerEdit.setText(flashcards[currentIndex].answer)
        }

        AlertDialog.Builder(this)
            .setTitle(if (isEdit) "Edit Flashcard" else "Add Flashcard")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val q = questionEdit.text.toString()
                val a = answerEdit.text.toString()
                if (q.isNotBlank() && a.isNotBlank()) {
                    if (isEdit) {
                        flashcards[currentIndex] = Flashcard(q, a)
                    } else {
                        flashcards.add(Flashcard(q, a))
                        currentIndex = flashcards.lastIndex
                    }
                    updateCard()
                } else {
                    Toast.makeText(this, "Please fill both fields", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
