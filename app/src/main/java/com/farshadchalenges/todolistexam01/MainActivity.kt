package com.farshadchalenges.todolistexam01

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farshadchalenges.todolistexam01.ui.theme.TODOListExam01Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TODOListExam01Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainPage()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage() {
    val focusManager = LocalFocusManager.current
    val myContext = LocalContext.current
    val userText = remember {
        mutableStateOf("")
    }
    val itemList = readData(myContext)
    val editDialogStatus = remember {
        mutableStateOf(false)
    }
    val deleteDialogStatus = remember {
        mutableStateOf(false)
    }
    val clickedItemIndex = remember {
        mutableStateOf(0)
    }
    val clickedItem = remember {
        mutableStateOf("")
    }
    val textDialogStatus = remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(7.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                value = userText.value,
                onValueChange = {
                    userText.value = it
                },
                label = { Text(text = "Enter Your TODO") },
                textStyle = TextStyle(fontSize = 18.sp, textAlign = TextAlign.Center),
                modifier = Modifier
                    .weight(7F)
                    .shadow(5.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(2.dp, Color.Black, RoundedCornerShape(10.dp))
                    .height(60.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    textColor = Color.White,
                    focusedLabelColor = Color.Green,
                    unfocusedLabelColor = Color.White,
                    cursorColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
            )
            Spacer(modifier = Modifier.padding(3.dp))
            Button(
                onClick = {
                    if (userText.value.isNotEmpty()) {
                        itemList.add(userText.value)
                        writeData(itemList, myContext)
                        focusManager.clearFocus()
                        userText.value = ""
                    }
                },
                shape = RectangleShape,
                modifier = Modifier
                    .height(60.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .weight(3f)
                    .border(2.dp, Color.Black, RoundedCornerShape(10.dp)),
            ) {
                Text(text = "Add", fontSize = 18.sp)
            }

        }

        LazyColumn {
            items(count = itemList.size, itemContent = { index ->

                val item = itemList[index]

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(1.dp), colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Color.DarkGray)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Text(
                            text = item,
                            color = Color.White,
                            fontSize = 20.sp,
                            fontFamily = FontFamily.SansSerif,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .width(250.dp)
                                .clickable {
                                    textDialogStatus.value = true
                                    clickedItem.value = item
                                }
                        )
                        Row(modifier = Modifier.width(80.dp)) {
                            IconButton(onClick = {
                                editDialogStatus.value = true
                                clickedItemIndex.value = index
                                clickedItem.value = item
                            }) {
                                Icon(
                                    Icons.Filled.Edit,
                                    contentDescription = "Edit",
                                    tint = Color.White
                                )
                            }
                            IconButton(onClick = {
                                deleteDialogStatus.value = true
                                clickedItemIndex.value = index
                            }) {
                                Icon(
                                    Icons.Filled.Delete,
                                    contentDescription = "Delete",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }
            )
        }
        if (deleteDialogStatus.value) {

            AlertDialog(
                onDismissRequest = { deleteDialogStatus.value = false },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Delete")
                        Icon(
                            Icons.Filled.Warning,
                            contentDescription = "",
                            tint = Color(0xFFC5AB5C)
                        )
                    }
                },
                text = {
                    Text(text = "Do you want to delete ?", fontSize = 16.sp)
                },

                confirmButton = {
                    TextButton(onClick = {
                        itemList.removeAt(clickedItemIndex.value)
                        writeData(itemList, myContext)
                        deleteDialogStatus.value = false
                        Toast.makeText(
                            myContext,
                            "You delete it", Toast.LENGTH_SHORT
                        ).show()
                    }) {
                        Text(text = "YES")
                    }
                },

                dismissButton = {
                    TextButton(onClick = {
                        deleteDialogStatus.value = false
                        Toast.makeText(
                            myContext,
                            "You Didn't delete it", Toast.LENGTH_SHORT
                        ).show()
                    }) {
                        Text(text = "NO")
                    }
                }
            )
        }
        if (editDialogStatus.value) {

            AlertDialog(
                onDismissRequest = { editDialogStatus.value = false },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Edit")
                        Icon(
                            Icons.Filled.Edit,
                            contentDescription = "",
                            tint = Color.White
                        )
                    }
                },

                text = {
                    TextField(value = clickedItem.value, onValueChange = { clickedItem.value = it })
                },

                confirmButton = {
                    TextButton(onClick = {
                        itemList[clickedItemIndex.value] = clickedItem.value
                        writeData(itemList, myContext)
                        editDialogStatus.value = false
                        Toast.makeText(
                            myContext,
                            "You Update it", Toast.LENGTH_SHORT
                        ).show()
                    }) {

                        Text(text = "Yes")
                    }
                },

                dismissButton = {
                    TextButton(onClick = {
                        editDialogStatus.value = false
                        Toast.makeText(
                            myContext,
                            "You Didn't Update it", Toast.LENGTH_SHORT
                        ).show()
                    }) {
                        Text(text = "No")
                    }
                }
            )
        }
        if (textDialogStatus.value) {

            AlertDialog(
                onDismissRequest = { textDialogStatus.value = false },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Text")
                        Icon(
                            Icons.Filled.Info,
                            contentDescription = "",
                            tint = Color(0xFFC5AB5C)
                        )
                    }
                },

                text = {
                   Text(text = clickedItem.value, fontSize = 16.sp)
                },

                confirmButton = {
                    TextButton(onClick = {
                        textDialogStatus.value = false
                    }) {

                        Text(text = "OK")
                    }
                }
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun MainPagePreview() {
    TODOListExam01Theme {
        MainPage()
    }
}