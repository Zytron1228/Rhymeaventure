import platform.posix.system
import kotlin.system.exitProcess
import kotlinx.cinterop.cValue
import platform.posix.nanosleep
import platform.posix.timespec

var path = ""

fun wait(time: Int) {
    var tsec = 0L
    var tmili = time
    if(time >= 1000) {
        do {
            tmili -= 1000
            tsec ++
        } while (tmili >= 1000)
    }
    val t = cValue<timespec> {
        tv_sec = tsec
        tv_nsec = tmili*1000000
    }
    nanosleep(t, null)
}
fun type(msg: String, pauseChar: String? = "║", pauseTime: Int = 500, intrvl: Int = 40) {
    msg.toCharArray().forEach { c: Char ->
        if(pauseChar != null && c.toString() == pauseChar) {
            wait(pauseTime)
        } else print(c)
        wait(intrvl)
    }
}
fun typeln(msg: String, pauseChar: String? = "║", pauseTime: Int = 500, intrvl: Int = 40, pauseAfter: Int = 125) {
    type(msg, pauseChar, pauseTime, intrvl)
    println()
    if(pauseAfter > 0) wait(pauseAfter)
}

enum class Types {
    Story, Input, //Finish, Death
}

class Event(path: String, story: String, options: Array<String>, type: Enum<Types> = Types.Story) {
    var stry = story
    var optns: Array<String> = options
//    var typ = type
}

val events = mapOf<String, Event>(
    Pair("",
        Event(
            path = "",
            story = "Hi there.║║ Why,║ Go ahead and choose ║Option 1.║ Thanks,║ bye bye my guy.",
            options = arrayOf(
                "Option A",
                "Option B",
                "Option C"
            )
        )
    ),
    Pair("NotFound",
        Event(
            path = "NotFound",
            story = "║ Pair(\n" +
                    "║     \"║NotFound║\",\n" +
                    "║     Event(\n" +
                    "║         path = \"║NotFound║\",\n" +
                    "║         story =║ \"Oh my, ║this part of the story,║ why,║ it seems to be missing║ or hasn't been written yet, no lie.║║ I wonder why.║\\n\" + \n" +
                    "║                  \"Maybe the pie was a lie?║║ Ask Bill Nye,║ He's the Science Guy.║║║║ You can restart the game if you'd like to.\\n\"║║║║║║ +\n" +
                    "║                  \"Why, those options dont work yet either.║.║.║║║ It's all a lie, just try║ not to cry.\",\n║║" +
                    "║         options = arrayOf(\n" +
                    "║             \"Restart game.\",║\n" +
                    "║             \"Restart game.\",║ \n" +
                    "║             \"There are no other options,\",║\n" +
                    "║             \"Just restart the game.\",║\n" +
                    "║             \"Do it.\",║\n" +
                    "║             \"Restart\", ║\n" +
                    "║             \"Because the choices║ are actually missing too.\"║\n" +
                    "║         )\n" +
                    "║     )\n" +
                    "║ ),",
            options = arrayOf(
                "Restart game.║",
                "Restart game.║",
                "There are no other options,║",
                "Just restart the game.║",
                "Do it.║",
                "Restart",
                "Because the choices are actually missing too.║"
            )
        )
    ),
    Pair("1",
        Event(
            path = "1",
            story = "You chose option 1.║ Good boy. \n║║║Oh my.║ Mini Luigi figures are raining from the sky!║║ ║I wonder why.║║ It must be the pie,║ I think it's a lie.║ Or maybe,║ I just forgot to say hi.║ Should I ask the guy in the tie? ║Or║ confront the pie? ║║*sigh*║║ I just can't let it fly.║ Or I just might die.║║",
            options = arrayOf(
                "Ask the guy in the tie.", //can I buy your tie
                "Confront the pie.", // "I will confront the pie," you decide. Is that guy getting pied? It seems he lied, and you got fried. Next time try to fallow the guide. Oh well, at least you tried, and didn't hide. That takes some pride, so take a ride down this slide, it's not too wide. Oh, sorry, you can't, you died.
                "Go back and say hi,║║ hey,║ I'm not that shy!║",
                "Why,║║ just let it fly!║║║║║", //You just might die. Oh My!
                "Bill║ Nye ║The ║Science ║Guy.║║║║™║" //science rules  bill! bill! bill! bill! bill!  Bill  Nye  the Science  Guy t- 20 seconds
            )
        )
    ),
    Pair("11",
        Event(
            path = "11",
            story = "You approach the guy in the tie║ from the side.║║ He is missing his right eye.║║ You asked the guy in the tie║ missing an eye,║ \"can I buy your tie?\"║ He looks at you,║ and struggles not to cry.║ ║He hands you a fry and says,║ \"that's why.\"║║ So you must try║ and stop the pie.║ You ask the guy,║ \"why?\" ║But he begins to cry,║ he cannot specify.║ ║All he can vocalise, is║ ║\"spaghettify.\"║ ║║║Is the pie║ a spy?║║║ Oh my!",
            options = arrayOf(
                "The pie is the spy.",
                "This guy║ is the spy.║",
                "Your making me hungry,║ I'll just eat that fry.║║║║║",
                "Oh my,║ forget about the fry,║║║ did that old woman over there║ just...║║║ swallow a fly?║"
            )
        )
    ),
    Pair("113",
        Event(
            path = "113",
            story = "Your are hungry, so you decide to eat the fry, but it tasted like a fly. Your not satisfied, you need more... or you may die. You decide the fry isn't enough for thy, and you go eat the pie. Now there is no spy, for you have also eaten the guy. But there is still another pie, for you have eaten an im-pot-ster pot-pie! Me-oh-my! You attempt to eat the pie, but suddenly. you realise that your actually living in a computer simul-",
            options = arrayOf(
                "A what?? Simulation?",
                "Suddenly what?",
                "No you finish the story, not me║",
                "Option 4. (Choose this to glitch the matrix.)"
            )
        )
    ),
    Pair("12",
        Event(
            path = "12",
            story = "\"I║ will confront the pie,\"║ you decide.║║║ You search for the pie,║ and you see a bride.║║ What's that║ to the side?║ Flying over the guide?║ Is she getting pied?║ You look back towards the ocean's tide,║ and see the pie║ approaching you,║ about to collide.║ ~Boom~║║\n║It seems he lied,║ and you just got fried.║ ║Next time,║ try to fallow the guide.║ Oh well,║ at least you tried, and didn't hide. ║That takes some pride,║ so take a ride║ down this slide,║ it's not too wide.║ Oh,║ sorry,║║ you can't,║ you died.║ Oh my,║ your spaghettified!║║ But what was that pie║ that would not abide║ by any rules of pride? ║You must decide.",
            options = arrayOf(
                "The pie was a lie.",
                "The pie was a spy."
            )
        )
    ),
    Pair("13",
        Event(
            path = "13",
            story = "║You go back to that one guy and say hi.║ You have proven to yourself║ that you are not very shy.║ The guy tells you that he has a reply.║ You ask him to specify,║ what kind of a reply?║ You ask for his reply,║ but he instead tells you why.║ ║But you zoned out,║ and he wont verify what he said,║ and he won't specify his reasoning for why.║║ Now║ your mouth is becoming very dry,║ and you begin║ to fry.║║║ You cry,║ \"why,║ ║why?\"║║║ Oh my║, you sure did try,║ but you let it fly,║ and now you must die.║ Was he a spy,║ working for the pie?║ And did he really know why?",
            options = arrayOf(
                "If I wasn't dry,║ I would cry.║",
                "Oh my,║ now I must die.║ And I can't say goodbye.",
                "I think someone here║ was telling a lie.",
                "Does swallowing a fly really hurt you?║║ And why?",
                "I'm gonna give up now.║ I have let you down.║ I should have ran around that guy║ and deserted him."
            )
        )
    ),
    Pair("14",
        Event(
            path = "14",
            story = "║You just might die. For that I cry.║ Why, oh my.║",
            options = arrayOf(
                "Don't Die.",
                "Die."
            )
        )
    ),
    Pair("141",
        Event(
            path = "141",
            story = "║You just might die. For that I cry.║ Why, oh my.║",
            options = arrayOf(
                "Don't Die.",
                "Die."
            )
        )
    ),
    Pair("1411",
        Event(
            path = "1411",
            story = "You║ don't║ die.║ Well,║ oh my!║",
            options = arrayOf(
                "ok║",
                "Die."
            )
        )
    ),
    Pair("14112",
        Event(
            path = "14112",
            story = "You║║ died.║║║ \n║║║Well you tried.║ Oh║ Well,║║ me oh my! Yee-high!║",
            options = arrayOf(
                "o║k",
                "o║k."
            )
        )
    ),
    Pair("2",
        Event(
            path = "2",
            story = "Yes .║ You are alive.║║ ║║║(the end)║║║║║",
            options = arrayOf(
                "Option A",
                "Option ║B."
            )
        )
    ),
    Pair("3",
        Event(
            path = "3",
            story = "Story yes.║ Lorem ipsum dolor sit amet,║ consectetur adipiscing elit.║ Mauris augue eros,║ congue eu molestie ac,║ dictum non nulla.║ Phasellus mi arcu,║ lacinia eu sodales et,║ hendrerit non justo.║ Ut quis║ varius nunc.║ Fusce║ finibus efficitur massa,║ sit amet tincidunt odio tempor et.║║ Fusce faucibus pharetra augue a pretium.║║ In suscipit sapien non tincidunt vestibulum.║║ Nam maximus,║ justo ac venenatis elementum,║ dolor magna fermentum dui,║ sit amet tincidunt augue leo in nibh.║║ Mauris quam urna,║║ volutpat vitae mattis sit amet,║║ scelerisque a ipsum.║║ Sed sit amet mattis odio.║║ Donec placerat eu mauris in egestas.║║ Mauris a euismod sapien.║║║ Non iens ut det te.║║",
            options = arrayOf(
                "No",
                "Yes",
                "What"
            )
        )
    )

)

val event: Event?
    get() = try {
        events.getValue(path)
    } catch (e: NoSuchElementException) {
        events.getValue("NotFound")
    }


fun getStoryFromPath(): String {

    return events.values.find { e: Event -> return@find e == event }?.stry
        ?: "Oh my, ║the dialogue for this part of the story seems to be missing or has not been written yet. You can restart the game if you'd like to. Actually those options dont work yet either... Just don't cry."

}

fun story(story: String, options: Array<String> = arrayOf("Option A", "Option B", "Option C"), clear: Boolean = true) {
    if(clear) system("cls")
    else println("\n\n\n\n")
    typeln("$story\n \n \n", intrvl = 21, pauseAfter = 1100)
    typeln("What will you do? \n║")
    wait(150)
    var optNum = 0
    for (option in options) {
        optNum++
        typeln("$optNum ) ║ $option", pauseTime = 150, pauseAfter = 325)
    }
    wait(150)
//        typeln("1 ) ║ Option A", pauseTime = 100, pauseAfter = 350)
//        typeln("2 ) ║ Option B", pauseTime = 100, pauseAfter = 350)
//        typeln("3 ) ║ Option C", pauseTime = 100, pauseAfter = 400)
    typeln("\nType the number for your selected response and hit enter.\n", intrvl = 25, pauseAfter = 200)
    type("\nYour Response: ")
    var w4i = true
    do {
        val input = readln().removePrefix(" ").removeSuffix(" ")
        try {
        when (input.toInt()) {
            in (1..optNum) -> {
                w4i = false
                path = "$path${input.toInt()}"
                println("\"$path\"")
                wait(800)
                story(getStoryFromPath(), options = getOptionsFromPath())
            }
            
            else -> {
                println("Please input one of the given answer choices(e.g. \"1\" \"2\" or \"3\").")
                wait(400)
                typeln("\nWhat will you do?║\n \n")
                typeln("Type the number for your selected answer and hit enter.\n", intrvl = 20, pauseAfter = 200)
                type("\n\nYour Response: ")
            }

        }
        } catch (e: NumberFormatException) {
            println("Please input one of the given answer choices(e.g. \"1\" \"2\" or \"3\").")
            wait(400)
            typeln("\nWhat will you do?║\n \n")
            typeln("Type the number for your selected answer and hit enter.\n", intrvl = 20, pauseAfter = 200)
            type("\n\nYour Response: ")
        } catch (e: Exception) {
            println("Please input one of the given answer choices(e.g. \"1\" \"2\" or \"3\" with no other characters). \nYou somehow triggered an unknown error with your response.")
            wait(400)
            typeln("\nWhat will you do?║\n \n")
            typeln("Type the number for your selected answer and hit enter.\n", intrvl = 20, pauseAfter = 200)
            type("\n\nYour Response: ")
        }
    } while (w4i)
}

fun getOptionsFromPath(): Array<String> {
    return events.values.find { it: Event -> return@find it == event }?.optns
        ?: arrayOf("Restart game.", "Restart game.", "There are no other options,", "Just restart the game.", "Do it.", "Restart", "Because the choices are missing.")

}

fun main() {
    var playing = false

//    fun template1() {
//        system("cls")
//        typeln("Story yes. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris augue eros, congue eu molestie ac, dictum non nulla. Phasellus mi arcu, lacinia eu sodales et, hendrerit non justo. Ut quis varius nunc. Fusce finibus efficitur massa, sit amet tincidunt odio tempor et. Fusce faucibus pharetra augue a pretium. In suscipit sapien non tincidunt vestibulum. Nam maximus, justo ac venenatis elementum, dolor magna fermentum dui, sit amet tincidunt augue leo in nibh. Mauris quam urna, volutpat vitae mattis sit amet, scelerisque a ipsum. Sed sit amet mattis odio. Donec placerat eu mauris in egestas. Mauris a euismod sapien. Non iens ut det te.\n   \n   \n")
//        wait(1000)
//        typeln("What will you do?        \n   \n")
//        wait(160)
//        typeln("1 )  Option A")
//        wait(250)
//        typeln("2 )  Option b")
//        wait(250)
//        typeln("3 )  Option C")
//        wait(215)
//        typeln(" \nType the number for your selected answer and hit enter.")
//        wait(75)
//        type("\n\nYour Response: ")cxc
//        var w4i = true
//        do {
//            when (readln().removePrefix(" ").removeSuffix(" ")) {
//                "1" -> {
//                    w4i = false
//                    typeln("uno")
//                    wait(900)
////                    template11()
//                }
//                "2" -> {
//                    w4i = false
//                    typeln("dose")
//                    wait(900)
////                    template12()
//                }
//                "3" -> {
//                    w4i = false
//                    typeln("tres")
//                    wait(900)
////                    template13()
//                }
//                else -> {
//                    println("Please input one of the given answer choices, \"1\" \"2\" or \"3\".")
//                    wait(400)
//                    typeln("\nWhat will you do?   \n \n")
//                    typeln("Type the number for your selected answer and hit enter.\n")
//                    type("\n\nYour Response: ")
//                }
//
//            }
//        } while (w4i)
//    }

    typeln("Hello, Kotlin/Native!")
    wait(900)
    typeln("Type whatever you want. Or type \"start\" to start the game. \n(type \"help\" for a full list of additional commands)\n", intrvl = 25)
    do {
        val inpt = readln()
        when (inpt.lowercase()) {
            "clear" -> {
                system("cls")
            }
            "end" -> {
                typeln("\nAre you sure you want to exit? [Y/N]", intrvl = 30)
                when (readln().lowercase()) {
                    "y" -> exitProcess(0)
                    "n" -> println("Canceled.\n")
                    else -> println("Expecting \"Y\" or \"N\". Assuming you meant \"n\" and canceling. Make sure you dont type any other letters, including space.")
                }
            }
            "start" -> {
                typeln("starting game.")
                wait(250)
                playing = true
                story(getStoryFromPath(), options = getOptionsFromPath())
//                story(story = "Story yes. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris augue eros, congue eu molestie ac, dictum non nulla. Phasellus mi arcu, lacinia eu sodales et, hendrerit non justo. Ut quis varius nunc. Fusce finibus efficitur massa, sit amet tincidunt odio tempor et. Fusce faucibus pharetra augue a pretium. In suscipit sapien non tincidunt vestibulum. Nam maximus, justo ac venenatis elementum, dolor magna fermentum dui, sit amet tincidunt augue leo in nibh. Mauris quam urna, volutpat vitae mattis sit amet, scelerisque a ipsum. Sed sit amet mattis odio. Donec placerat eu mauris in egestas. Mauris a euismod sapien. Non iens ut det te. ")
            }
            "help" -> println("\nclear : Clears this console.\n\n" +
                    "start : Starts the game.\n\n" +
                    "end : Asks to confirm exiting the application. Closes the application if user responds with \"Y\" and cancels if \"N\" or if the response is neither. (i.e. user typed \"Y \"(with a space) or \"Yes\" or \"M\" etc.)\n\n" +
                    "execute : Executes the command typed after \"execute \" if it is valid.\nType \"execute help\" for help with these commands.\n\n" +
                    "help : This.\n")
            else -> {
                println()
                if(inpt.startsWith("execute ", true)) {
                    system(inpt.removePrefix("execute "))
                }
            }
        }
    } while (!playing)
    typeln("press `enter` to exit.  The program has ended.")
    readln()
}