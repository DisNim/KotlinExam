package com.example.ex7

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ex7.ui.theme.EX7Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            View()

        }
    }
}

@Composable
fun View() {
    var studentName by remember { mutableStateOf("") }
    var studentSurname by remember {mutableStateOf("")}
    var studentGrade by remember { mutableStateOf("") }
    var listBestStudents by remember {mutableStateOf<List<BestStudentData>>(listOf())}
    var listStudents by remember { mutableStateOf(mutableListOf<StudentData>()) }

    Column(modifier = Modifier.fillMaxSize().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {

        OutlinedTextField(value = studentSurname, onValueChange = {studentSurname = it},
            placeholder = {Text("Фамилия")})
        Spacer(modifier = Modifier.height(30.dp))
        OutlinedTextField(value = studentName, onValueChange = {studentName = it},
            placeholder = {Text("Имя")})
        Spacer(modifier = Modifier.height(30.dp))
        OutlinedTextField(value = studentGrade, onValueChange = {studentGrade = it},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = {Text("Оценка")})

        Spacer(modifier = Modifier.height(30.dp))
        Button(onClick = {listStudents.add(StudentData(studentSurname, studentName, studentGrade.toInt()))}) {Text("Добавить")}
        Button(onClick = {listBestStudents = getBestThreeStudent(listStudents)}) {Text("Вывести лучшею тройку")}

        LazyColumn {
            items(listBestStudents){item -> Text("${item.surname} ${item.name} ${item.avgGrade}")}
        }
    }
}

fun getBestThreeStudent(studentList: List<StudentData>): List<BestStudentData>{
    var studentWithAvg = mutableListOf<BestStudentData>()
    var byName = studentList.groupBy { Pair(it.surname, it.name) }
    for ((key, value) in byName){
        val avg = value.map{it.grade}.average()
        studentWithAvg.add(BestStudentData(key.first, key.second, avg))
    }
    return studentWithAvg.sortedByDescending { it.avgGrade }.take(3)
}
data class StudentData(val surname:String, val name:String, val grade:Int)
data class BestStudentData(val surname:String, val name:String, val avgGrade:Double)
@Preview(showBackground = true)
@Composable
fun ViewPreview() {
    View()
}