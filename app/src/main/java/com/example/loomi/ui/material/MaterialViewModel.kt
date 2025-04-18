package com.example.loomi.ui.material

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.loomi.model.Content
import com.example.loomi.model.ContentType
import com.example.loomi.model.Material
import com.example.loomi.model.Section

class MaterialViewModel : ViewModel(){
    private val _materials = MutableLiveData<List<Material>>()
    val materials: LiveData<List<Material>> get() = _materials

    init {
        loadDummyData()
    }

    private fun loadDummyData() {
        _materials.value = listOf(
            Material(
                id = "1",
                title = "Kotlin Fundamental",
                imgResId = null,
                sections = listOf(
                    Section(
                        id = "1",
                        title = "Variables",
                        content = listOf(
                            Content(
                                id = "1",
                                ContentType.EXPLANATION,
                                title = "Variables",
                                description = "Variables are containers for storing data values.\n" +
                                        "\n" +
                                        "To create a variable, use var or val, and assign a value to it with the equal sign (=):"
                            ),
                            Content(
                                id = "2",
                                ContentType.CODE_SNIPPET,
                                title = "Code Snippet",
                                description = "var name: String = \"John\"\n" +
                                        "val age: Int = 30\n" +
                                        "val isStudent: Boolean = true"
                            ),
                            Content(
                                id = "3",
                                ContentType.FILL_IN_BLANK,
                                title = "Fill in the Blank",
                                description = "To declare a variable in Kotlin, use the keyword ___ followed by the variable name and type."
                            ),
                            Content(
                                id = "4",
                                ContentType.MULTIPLE_CHOICE,
                                title = "Multiple Choice",
                                description = "What is the correct way to declare a variable in Kotlin?",
                                options = listOf(
                                    "var name: String = \"John\"",
                                    "val name: String = \"John\"",
                                    "var name: Int = 30",
                                    "val name: Boolean = true"
                                ),
                                answer = "var name: String = \"John\""
                            ),
                            Content(
                                id = "5",
                                ContentType.RESULT,
                                title = "Result",
                                description = "Correct! In Kotlin, you can declare a variable using var or val, followed by the variable name and type."
                            )

                        )
                    ),
                    Section(
                        id = "2",
                        title = "Data Types",
                        content = listOf(
                            Content(
                                id = "2",
                                ContentType.FILL_IN_BLANK,
                                title = "Tipe Data",
                                description = "In Kotlin, the type of a variable is decided by its value:"
                            ),
                            Content(
                                id = "3",
                                ContentType.MULTIPLE_CHOICE,
                                title = "Multiple Choice",
                                description = "What is the correct way to declare a variable in Kotlin?",
                                options = listOf(
                                    "var name: String = \"John\"",
                                    "val name: String = \"John\"",
                                    "var name: Int = 30",
                                    "val name: Boolean = true"
                                ),
                                answer = "var name: String = \"John\""
                            ),
                            Content(
                                id = "4",
                                ContentType.RESULT,
                                title = "Result",
                                description = "Correct! In Kotlin, you can declare a variable using var or val, followed by the variable name and type."
                            ),
                            Content(
                                id = "5",
                                ContentType.CODE_SNIPPET,
                                title = "Code Snippet",
                                description = "var name: String = \"John\"\n" +
                                        "val age: Int = 30\n" +
                                        "val isStudent: Boolean = true"
                            ),
                            Content(
                                id = "6",
                                ContentType.EXPLANATION,
                                title = "Tipe Data",
                                description = "In Kotlin, the type of a variable is decided by its value:\n" +
                                        "\n" +
                                        "1. Int: Integer values\n" +
                                        "2. Double: Decimal values\n" +
                                        "3. String: Text values\n" +
                                        "4. Boolean: True or false values"
                            ),
                        ),

                    ),
                    Section(
                        id = "3",
                        title = "String",
                        content = listOf(
                            Content(
                                id = "3",
                                ContentType.EXPLANATION,
                                title = "String",
                                description = "A string is a sequence of characters, enclosed in double quotes."
                            ),
                            Content(
                                id = "4",
                                ContentType.CODE_SNIPPET,
                                title = "Code Snippet",
                                description = "val name: String = \"John\"\n" +
                                        "val age: Int = 30\n" +
                                        "val isStudent: Boolean = true"
                            ),
                            Content(
                                id = "5",
                                ContentType.FILL_IN_BLANK,
                                title = "Fill in the Blank",
                                description = "To declare a string variable in Kotlin, use the keyword ___ followed by the variable name and type."
                            ),
                            Content(
                                id = "6",
                                ContentType.MULTIPLE_CHOICE,
                                title = "Multiple Choice",
                                description = "What is the correct way to declare a string variable in Kotlin?",
                                options = listOf(
                                    "var name: String = \"John\"",
                                    "val name: String = \"John\"",
                                    "var name: Int = 30",
                                    "val name: Boolean = true"
                                ),
                                answer = "var name: String = \"John\""
                            ),
                            Content(
                                id = "7",
                                ContentType.RESULT,
                                title = "Result",
                                description = "Correct! In Kotlin, you can declare a string variable using var or val, followed by the variable name and type."
                            ),
                        )
                    ),
                    Section(
                        id = "4",
                        title = "Operator",
                        content = listOf(
                            Content(
                                id = "4",
                                ContentType.EXPLANATION,
                                title = "Operator",
                                description = "In Kotlin, the type of a variable is decided by its value:"
                            ),
                            Content(
                                id = "5",
                                ContentType.CODE_SNIPPET,
                                title = "Code Snippet",
                                description = "var name: String = \"John\"\n" +
                                        "val age: Int = 30\n" +
                                        "val isStudent: Boolean = true"
                            ),
                            Content(
                                id = "6",
                                ContentType.FILL_IN_BLANK,
                                title = "Fill in the Blank",
                                description = "To declare a string variable in Kotlin, use the keyword ___ followed by the variable name and type."
                            ),
                            Content(
                                id = "7",
                                ContentType.MULTIPLE_CHOICE,
                                title = "Multiple Choice",
                                description = "What is the correct way to declare a string variable in Kotlin?",
                                options = listOf(
                                    "var name: String = \"John\"",
                                    "val name: String = \"John\"",
                                    "var name: Int = 30",
                                    "val name: Boolean = true"
                                ),
                                answer = "var name: String = \"John\""
                            ),
                            Content(
                                id = "8",
                                ContentType.RESULT,
                                title = "Result",
                                description = "Correct! In Kotlin, you can declare a string variable using var or val, followed by the variable name and type."
                            ),
                        )
                    ),
                )
            ),
            Material(
                id = "2",
                title = "Control Flow",
                imgResId = null,
                sections = listOf(
                    Section(
                        id = "1",
                        title = "Control Flow",
                        content = listOf(
                            Content(
                                id = "1",
                                ContentType.EXPLANATION,
                                title = "If-Else",
                                description = "An if-else statement is a conditional statement that executes a block of code if a certain condition is true."
                            ),
                            Content(
                                id = "2",
                                ContentType.CODE_SNIPPET,
                                title = "Code Snippet",
                                description = "if (condition) {\n" +
                                        "    // code to execute if condition is true\n" +
                                        "} else {\n" +
                                        "    // code to execute if condition is false\n" +
                                        "}"
                            ),
                            Content(
                                id = "3",
                                ContentType.FILL_IN_BLANK,
                                title = "Fill in the Blank",
                                description = "To declare a variable in Kotlin, use the keyword ___ followed by the variable name and type."
                            ),
                            Content(
                                id = "4",
                                ContentType.MULTIPLE_CHOICE,
                                title = "Multiple Choice",
                                description = "What is the correct way to declare a variable in Kotlin?",
                                options = listOf(
                                    "var name: String = \"John\"",
                                    "val name: String = \"John\"",
                                    "var name: Int = 30",
                                    "val name: Boolean = true"
                                ),
                                answer = "var name: String = \"John\""
                            ),
                            Content(
                                id = "5",
                                ContentType.RESULT,
                                title = "Result",
                                description = "Correct! In Kotlin, you can declare a variable using var or val, followed by the variable name and type."
                            ),
                        )
                    ),
                    Section(
                        id = "2",
                        title = "When",
                        content = listOf(
                            Content(
                                id = "2",
                                ContentType.EXPLANATION,
                                title = "When",
                                description = "A when statement is a conditional statement that executes a block of code based on the value of a variable or expression."
                            ),
                            Content(
                                id = "3",
                                ContentType.CODE_SNIPPET,
                                title = "Code Snippet",
                                description = "when (variable) {\n" +
                                        "    value1 -> // code to execute if variable == value1\n" +
                                        "    value2 -> // code to execute if variable == value2\n" +
                                        "    else -> // code to execute if variable does not match any values\n" +
                                        "}"
                            ),
                            Content(
                                id = "4",
                                ContentType.FILL_IN_BLANK,
                                title = "Fill in the Blank",
                                description = "To declare a variable in Kotlin, use the keyword ___ followed by the variable name and type."
                            ),
                            Content(
                                id = "5",
                                ContentType.MULTIPLE_CHOICE,
                                title = "Multiple Choice",
                                description = "What is the correct way to declare a variable in Kotlin?",
                                options = listOf(
                                    "var name: String = \"John\"",
                                    "val name: String = \"John\"",
                                    "var name: Int = 30",
                                    "val name: Boolean = true"
                                ),
                                answer = "var name: String = \"John\""
                            ),
                            Content(
                                id = "6",
                                ContentType.RESULT,
                                title = "Result",
                                description = "Correct! In Kotlin, you can declare a variable using var or val, followed by the variable name and type."
                            ),
                        )
                    ),
                    Section(
                        id = "3",
                        title = "For Loop",
                        content = listOf(
                            Content(
                                id = "3",
                                ContentType.EXPLANATION,
                                title = "For Loop",
                                description = "A for loop is used to iterate over a range of values or a collection."
                            ),
                            Content(
                                id = "4",
                                ContentType.CODE_SNIPPET,
                                title = "Code Snippet",
                                description = "for (i in 1..10) {\n" +
                                        "    // code to execute for each value of i\n" +
                                        "}"
                            ),
                            Content(
                                id = "5",
                                ContentType.FILL_IN_BLANK,
                                title = "Fill in the Blank",
                                description = "To declare a variable in Kotlin, use the keyword ___ followed by the variable name and type."
                            ),
                            Content(
                                id = "6",
                                ContentType.MULTIPLE_CHOICE,
                                title = "Multiple Choice",
                                description = "What is the correct way to declare a variable in Kotlin?",
                                options = listOf(
                                    "var name: String = \"John\"",
                                    "val name: String = \"John\"",
                                    "var name: Int = 30",
                                    "val name: Boolean = true"
                            ),
                                answer = "var name: String = \"John\""
                            ),
                        )
                    ),
                    Section(
                        id = "4",
                        title = "While Loop",
                        content = listOf(
                            Content(
                                id = "1",
                                ContentType.EXPLANATION,
                                title = "While Loop",
                                description = "A while loop is used to repeat a block of code as long as a certain condition is true."
                            ),
                            Content(
                                id = "2",
                                ContentType.CODE_SNIPPET,
                                title = "Code Snippet",
                                description = "while (condition) {\n" +
                                        "    // code to execute while condition is true\n" +
                                        "}"
                            ),
                            Content(
                                id = "3",
                                ContentType.FILL_IN_BLANK,
                                title = "Fill in the Blank",
                                description = "To declare a variable in Kotlin, use the keyword ___ followed by the variable name and type."
                            ),
                            Content(
                                id = "4",
                                ContentType.MULTIPLE_CHOICE,
                                title = "Multiple Choice",
                                description = "What is the correct way to declare a variable in Kotlin?",
                                options = listOf(
                                    "var name: String = \"John\"",
                                    "val name: String = \"John\"",
                                    "var name: Int = 30",
                                    "val name: Boolean = true"
                                ),
                                answer = "var name: String = \"John\""
                            ),
                            Content(
                                id = "5",
                                ContentType.RESULT,
                                title = "Result",
                                description = "Correct! In Kotlin, you can declare a variable using var or val, followed by the variable name and type."
                            ),
                        )
                    ),
                )
            ),
            Material(
                id = "3",
                title = "Functions",
                imgResId = null,
                sections = listOf(
                    Section(
                        id = "1",
                        title = "Functions",
                        content = listOf(
                            Content(
                                id = "1",
                                ContentType.EXPLANATION,
                                title = "Functions",
                                description = "A function is a group of statements that performs a specific task or calculates a value. Functions can take input parameters and return a value.\n" +
                                        "\n" +
                                        "To declare a function, use the fun keyword, followed by the function name, a list of parameters, and a return type (if applicable)."
                            ),
                            Content(
                                id = "2",
                                ContentType.CODE_SNIPPET,
                                title = "Code Snippet",
                                description = "fun functionName(parameter1: Type1, parameter2: Type2): ReturnType {\n" +
                                        "    // code to execute\n" +
                                        "}"
                            ),
                        )
                    ),
                    Section(
                        id = "2",
                        title = "Function Parameters",
                        content = listOf(
                            Content(
                                id = "2",
                                ContentType.EXPLANATION,
                                title = "Parameters",
                                description = "Parameters are variables that are passed to a function when it is called.\n" +
                                        "\n" +
                                        "To pass a parameter to a function, you can use the function name followed by the parameter name, separated by a comma."
                            ),
                            Content(
                                id = "3",
                                ContentType.CODE_SNIPPET,
                                title = "Code Snippet",
                                description = "fun functionName(parameter1: Type1, parameter2: Type2): ReturnType {\n" +
                                        "    // code to execute\n" +
                                        "}"
                            ),
                            Content(
                                id = "4",
                                ContentType.FILL_IN_BLANK,
                                title = "Fill in the Blank",
                                description = "To declare a function in Kotlin, use the keyword ___ followed by the function name and parameters."
                            ),
                            Content(
                                id = "5",
                                ContentType.MULTIPLE_CHOICE,
                                title = "Multiple Choice",
                                description = "What is the correct way to declare a function in Kotlin?",
                                options = listOf(
                                    "fun functionName(parameter1: Type1, parameter2: Type2): ReturnType",
                                    "function functionName(parameter1: Type1, parameter2: Type2): ReturnType",
                                    "def functionName(parameter1: Type1, parameter2: Type2): ReturnType",
                                    "function functionName(parameter1: Type1, parameter2: Type2): ReturnType { }"
                                ),
                                answer = "fun functionName(parameter1: Type1, parameter2: Type2): ReturnType"
                            ),
                            Content(
                                id = "6",
                                ContentType.RESULT,
                                title = "Result",
                                description = "Correct! In Kotlin, you can declare a function using the fun keyword, followed by the function name and parameters."
                            ),
                        )
                    ),
                    Section(
                        id = "3",
                        title = "Function Return",
                        content = listOf(
                            Content(
                                id = " 1",
                                ContentType.EXPLANATION,
                                title = "Return",
                                description = "A function can return a value using the return keyword.\n" +
                                        "\n" +
                                        "To return a value from a function, use the return keyword followed by the value to be returned."
                            ),
                            Content(
                                id = "2",
                                ContentType.CODE_SNIPPET,
                                title = "Code Snippet",
                                description = "fun functionName(parameter1: Type1, parameter2: Type2): ReturnType {\n" +
                                        "    // code to execute\n" +
                                        "    return value\n" +
                                        "}"
                            ),
                            Content(
                                id = "3",
                                ContentType.FILL_IN_BLANK,
                                title = "Fill in the Blank",
                                description = "To declare a function in Kotlin, use the keyword ___ followed by the function name and parameters."
                            ),
                            Content(
                                id = "4",
                                ContentType.MULTIPLE_CHOICE,
                                title = "Multiple Choice",
                                description = "What is the correct way to declare a function in Kotlin?",
                                options = listOf(
                                    "fun functionName(parameter1: Type1, parameter2: Type2): ReturnType",
                                    "function functionName(parameter1: Type1, parameter2: Type2): ReturnType",
                                    "def functionName(parameter1: Type1, parameter2: Type2): ReturnType",
                                    "function functionName(parameter1: Type1, parameter2: Type2): ReturnType { }"
                                ),
                                answer = "fun functionName(parameter1: Type1, parameter2: Type2): ReturnType"
                            ),
                            Content(
                                id = "5",
                                ContentType.RESULT,
                                title = "Result",
                                description = "Correct! In Kotlin, you can declare a function using the fun keyword, followed by the function name and parameters."
                            ),
                        )
                    ),
                )
            )
        )
    }
}