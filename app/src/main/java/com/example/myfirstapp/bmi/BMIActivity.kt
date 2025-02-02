package com.example.myfirstapp.bmi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myfirstapp.R
import com.example.myfirstapp.firebase.FireStore
import com.example.myfirstapp.mainViews.MainUser
import com.google.firebase.auth.FirebaseAuth
/**
 * BMIActivity is an activity that allows users to calculate their Body Mass Index (BMI)
 * based on their height and weight. It also provides a classification of the BMI result.
 */
class BMIActivity : AppCompatActivity() {
    private lateinit var inputHeight: EditText
    private lateinit var inputWeight: EditText
    private lateinit var result: TextView
    private lateinit var calculateButton: Button
    private lateinit var goBack: Button

    private val auth = FirebaseAuth.getInstance()
    val email = FirebaseAuth.getInstance().currentUser?.email.orEmpty()
    /**
     * Function initializes the activity, sets up UI components, and handles button clicks.
     * @param savedInstanceState A Bundle containing the activity's previously saved state.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_bmiactivity)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val userId=auth.currentUser?.uid
        setup()


        goBack.setOnClickListener {
            val intent = Intent(this, MainUser::class.java)
            startActivity(intent)
        }

        calculateButton.setOnClickListener {
          calculte(inputHeight,inputWeight,result)
        }
    }
    /**
     * Function calculates the BMI based on the given height and weight input fields.
     * @param inheight The EditText containing the user's height in centimeters.
     * @param inweight The EditText containing the user's weight in kilograms.
     * @param result The TextView where the BMI result will be displayed.
     */
    fun calculte(inheight:EditText,inweight:EditText,result: TextView){
        val height=inheight.text.toString().toFloatOrNull()
        val weight=inweight.text.toString().toFloatOrNull()

        if ((height==null) ||( weight==null) ){
            result.text = "Please enter both height and weight."
            return
        }
        if(height <= 0 || weight <= 0){
            result.text= "Height and weight must be positive numbers"
            return
        }
        val bmi = weight/(height/100 * height/100)
        val message = displayMessage(bmi)
        result.text = String.format("Your BMI: %.2f $message", bmi)
    }
    /**
     * Function determines the BMI category based on the calculated BMI value.
     * @param bmi The calculated Body Mass Index value.
     * @return A string message indicating the BMI category.
     */
    fun displayMessage(bmi:Float):String{
        return when {
            bmi < 18.5 -> "You are underweight."
            bmi < 24.9 -> "You have a normal weight."
            bmi < 29.9 -> "You are overweight."
            else -> "You are obese."
        }
    }
    /**
     * Initializes UI elements.
     */
    private fun setup(){
        inputHeight = findViewById(R.id.height)
        inputWeight = findViewById(R.id.weight)
        result = findViewById(R.id.result)
        calculateButton = findViewById(R.id.calculateBmi)
        goBack = findViewById(R.id.goBack)
    }

}