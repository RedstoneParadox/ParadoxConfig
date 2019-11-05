package redstoneparadox.paradoxconfig.test

fun runTests() {
    println("Is the test successful? ${TestConfig.test}")
    println("Value of the range option: ${TestConfig.testTwo}")
    println("Hi? ${TestConfig.InnerTestConfig.testThree}")

    for (greeting in TestConfig.collection) {
        println(greeting)
    }

    for ((name, number) in TestConfig.dictionary) {
        println("$name to $number")
    }
}

