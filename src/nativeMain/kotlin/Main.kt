import platform.posix.system
import kotlin.system.exitProcess
import kotlinx.cinterop.cValue
import platform.posix.nanosleep
import platform.posix.timespec

var path = ""

fun wait(time: Int) {// time = milliseconds to wait. 1000 millisec = 1 sec.
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
fun typeln(msg: String, pauseChar: String? = "║", pauseTime: Int = 400, intrvl: Int = 40, pauseAfter: Int = 125) {
    type(msg, pauseChar, pauseTime, intrvl)
    println()
    if(pauseAfter > 0) wait(pauseAfter)
}

enum class Types {
    Story, Input, Finish, Death, Menu, TextOnly
}

class Event(/*path: String, */story: String, options: Array<String>, type: Enum<Types> = Types.Story, performsAction: Boolean = false, action: () -> Unit = {}, actionTime: Int = 1) {
    var stry = story
    var optns: Array<String> = options
    var typ = type
    var performsAct = performsAction
    var actn = action
    var actnTm = actionTime
}

val events = mapOf<String, Event>(
    Pair("", //story "path" identifier. Does not show up anywhere in game. When you start game, if you choose option 2, then 3, then 4, then 3, then 1, then the "path" id for the next event that you are being taken to because you just chose "1", is "23431" so say you want to add story event for what happens when you are at path "1422" and then choose option "3", the path id for what happens next is "14223" (aka old path + option number you chose). the rest is automatic. Told you that system is useful. All you need is a number that matches how you get there and the system code know when to display it, and takes strings you put here and automatically prints those in a preset way, automatically.
        Event( //you can remove everything after that "//". This is a note. Put a "║" to indicate a 0.5 second pause. multiple ║s in a row work. "║║║║║" would be a 2.5 sec delay. "\n" means a new line. Don't actually just go to a new line in the file, but there is a specific format for that I can show you if you want to put the string on multiple lines so you dont have to scroll left and right in the file like you are here. I usually add a "║" at new lines and periods, so you might too. "\\" means "\". "\"" (\") means """ (").  Side note: that is a backslash (\) not forward slash (/) the "What will you do next? Type the number" line is ALREADY THERE in THE CODE separate from this, along with the given option numbers.
            story = "Hi there.║║ Pick an adventure to play through.", //try to keep a rhyme at the end of almost every sentence if your making a 'event' for that part of the story.
            options = arrayOf( //you can have between 1 and 9 options. tell me if you need to add more, I think I know how I can compensate. they do not need to be on separate lines, but it looks nice, and they DO need a "," after the quotes EXCEPT for the last one. Make sure you dont accidentally remove the "( )"s
                "The Rhymes Of Why",
                "[Unnamed Adventure]",
                "The Muffin Man(continuation)",
                "Gigachad Quest",
                "No I wont pick one."
            )
        )
    ),
    Pair("NotFound",
        Event(
            story = "║ Pair(\n" +
                    "║     \"║NotFound║║\",\n" +
                    "║     Event(\n" +
                    "║         path = \"║NotFound║║\",\n" +
                    "║         story =║ \"Oh my, ║this part of the story,║ why,║ it seems to be missing║ or hasn't been written yet, no lie.║║ I wonder why.║\\n\" + \n" +
                    "║                  \"Maybe the pie was a lie?║║ Ask Bill Nye,║ He's the Science Guy.║║║║ You can restart the game if you'd like to.\\n\"║║║║║ +\n" +
                    "║                  \"Why, those options dont work yet either.║.║.║║║ Just kidding, I fixed that.║║ It's all a lie, just try║ not to cry.\",║║║\n║" +
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
            story = "You chose option 1. \n║║║Oh my.║ Mini Luigi figures are raining from the sky!║║ ║I wonder why.║║ It must be the pie,║ I think it's a lie.║ Or maybe,║ I just forgot to say hi.║ Should I ask the guy in the tie? ║Or║ confront the pie? ║║*sigh*║║ I just can't let it fly.║ Or I just might die.║║",
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
            story = "\"I║ will confront the pie,\"║ you decide.║║║ You search for the pie,║ and you see a bride.║║ What's that║ to the side?║ Flying over the guide?║ Is she getting pied?║ You look back towards the ocean's tide,║ and see the pie║ approaching you,║ about to collide.║ ~Boom~║║\n║It seems he lied,║ and you just got fried.║ ║Next time,║ try to fallow the guide.║ Oh well,║ at least you tried, and didn't hide. ║That takes some pride,║ so take a ride║ down this slide,║ it's not too wide.║ Oh,║ sorry,║║ you can't,║ you died.║ Oh my,║ your spaghettified!║║ But what was that pie║ that would not abide║ by any rules of pride? ║You must decide.",
            options = arrayOf(
                "The pie was a lie.",
                "The pie was a spy."
            )
        )
    ),
    Pair("13",
        Event(
            story = "║You go back to that one guy and say hi.║ You have proven to yourself║ that you are not very shy.║║ The guy tells you that he has a reply.║ You ask him to specify,║ what kind of a reply?║ You ask for his reply,║ but he instead tells you why.║ ║But you zoned out,║ and he wont verify what he said,║ and he won't specify his reasoning for why.║║ Now║ your mouth is becoming very dry,║ and you begin║ to fry.║║║ You cry,║ \"why,║ ║why?\"║║║ Oh my║, you sure did try,║ but you let it fly,║ and now you must die.║ Was he a spy,║ working for the pie?║ And did he really know why?",
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
            story = "║You just might die. For that I cry.║ Why, oh my.║",
            options = arrayOf(
                "Don't Die.",
                "Die."
            )
        )
    ),
    Pair("141",
        Event(
            story = "║You just might die. For that I cry.║ Why, oh my.║",
            options = arrayOf(
                "Don't Die.",
                "Die."
            )
        )
    ),
    Pair("1411",
        Event(
            story = "You║ don't║ die.║ Well,║ oh my!║",
            options = arrayOf(
                "ok║",
                "Die."
            )
        )
    ),
    Pair("1412",
        Event(
            story = "Oh my!║║ You.║║ died.║║ anyways.║║║ \n║║║Well you tried,║ or not.║║ Oh║ Well.║",
            options = arrayOf(
                "o║k",
                "o║k."
            )
        )
    ),
    Pair("14112",
        Event(
            story = "You║║ died.║║║ \n║║║Oh my! Well║ you tried.║║ Oh║ Well.║",
            options = arrayOf(
                "o║k",
                "o║k."
            ),
            type = Types.Death
        )
    ),
    Pair("15",
        Event(
            story = "║BILL  NYE  THE SCIENCE  GUY!!!!!!!!\n" +
                    "Bill Nye, the Science Guy!║\n" +
                    "Bill,║ Bill,║ Bill\n" +
                    "Bill,║ Bill,║ Bill\n" +
                    "Bill Nye,║ the Science Guy!║║\n" +
                    "Science rules║║║║\n" +
                    "Bill Nye,║ the Science Guy!║\n" +
                    "Inertia is a property of matter║\n" +
                    "Bill,║ Bill,║ Bill,║ Bill║\n" +
                    "Bill,║ Bill,║║║ Bill Nye, the Science Guy!║║\n" +
                    "Bill, Bill, Bill, Bill, (Pi minus ten seconds)\n" +
                    "Bill, Bill, Bill, Bill, Bill, Bill\n" +
                    "Bill Nye, the Science Guy!",

            options = arrayOf(
                "Option A",
                "Option ║B."
            ),
            performsAction = true,
            action = {
//                system("https://www.youtube.com/watch?v=YdrPb3QderI") // <--- doesn't work
                //bill nye screaming guy
            }
        )
    ),
    Pair("2",
        Event(
            story = "║E║║\n(the end)║║║║║",
            options = arrayOf(
                "Ok",
                "Ok║║║║║║",
                "Chad."
            )
        )
    ),
    Pair("23",
        Event(
            story = "Chad.",
            options = arrayOf(
                "Ok.║",
                "Chad."
            )
        )
    ),
    Pair("231",
        Event(
            story = "Ok.",
            options = arrayOf(
                "Ok.",
                "Ok."
            ),
            performsAction = true,
            action = {
                path = "231"
            },
            actionTime = 7
        )
    ),
    Pair("232",
        Event(
            story = when ((1..5).random()) {
                in 1..4 -> "Chad."
                else -> "Giga Chad."
            },
            options = arrayOf(
                when ((1..8).random()) {
                    in 1..7 -> "Chad."
                    else -> "Giga Chad."
                },
                when ((1..9).random()) {
                    in 1..8 -> "Chad."
                    else -> "Giga Chad."
                }
            ),
            performsAction = true,
            action = {
                path =
                    when ((1..20).random()) {
                        in 1..19 -> "232"
                        else -> "232chad"
                    }
            },
            actionTime = 7
        )
    ),
    Pair("232chad",
        Event(
            story = "You earned the Giga Chad ending. Such Chad.\nPress 1 to restart the game.",
            options = arrayOf(
                "Restart"
            ),
            performsAction = true,
            action = {
                path = ""
            },
            actionTime = 7
        )
    ),
    Pair("3",
        Event(
            story = "Never gonna give you up║║\n" +
                    "Never gonna let you down║║\n" +
                    "Never gonna run║ around║ and║ desert you║║\n" +
                    "Never gonna make you cry║║\n" +
                    "Never gonna say║ goodbye║║\n" +
                    "Never gonna tell a lie║║ and hurt you║",
            options = arrayOf(
                "Give him up",
                "Let him down",
                "Run around, Desert Him.",
                "Make him cry",
                "Say goodbye",
                "Tell him a lie, and then hurt him.",
                "All of the above",
                "Die",
                "I thought you said this was the new continuation of \"The Muffin Man\""
            )
        )
    ),
    Pair("38",
        Event(
            story = "You might be about to die, that's not good.",
            options = arrayOf(
                "Die",
                "Don't Die."
            )
        )
    ),
    Pair("381",
        Event(
            story = "How would you like to die? ",
            options = arrayOf(
                "Die",
                "Death by death",
                "Error 404: Not Found"
            )
        )
    ),
    Pair("3811",
        Event(
            story = "You might be about to die, that's not good.",
            options = arrayOf(
                "Die",
                "Don't Die."
            )
        )
    ),
    Pair("38111",
        Event(
            story = "You might be about to die, that's not good.",
            options = arrayOf(
                "Die",
                "Don't Die."
            )
        )
    ),
    Pair("381111",
        Event(
            story = "You might be about to die, that's not good.",
            options = arrayOf(
                "Die",
                "Don't Die."
            )
        )
    ),
    Pair("3812",
        Event(
            story = "You might be about to die, that's not good.",
            options = arrayOf(
                "Die",
                "Don't Die."
            )
        )
    ),
    Pair("38121",
        Event(
            story = "You might be about to die, that's not good.",
            options = arrayOf(
                "Die",
                "Don't Die."
            )
        )
    ),
    Pair("381211",
        Event(
            story = "You might be about to die, that's not good.",
            options = arrayOf(
                "Die",
                "Don't Die."
            )
        )
    ),
    Pair("382",
        Event(
            story = "You might be about to die, that's not good.",
            options = arrayOf(
                "Die",
                "Don't Die."
            )
        )
    ),
    Pair("3821",
        Event(
            story = "You might be about to die, that's not good.",
            options = arrayOf(
                "Die",
                "Don't Die."
            )
        )
    ),
    Pair("38211",
        Event(
            story = "You might be about to die, that's not good.",
            options = arrayOf(
                "Die",
                "Don't Die."
            )
        )
    ),
    Pair("382111",
        Event(
            story = "You might be about to die, that's not good.",
            options = arrayOf(
                "Die",
                "Don't Die."
            )
        )
    ),
    Pair("3821111",
        Event(
            story = "You might be about to die, that's not good.",
            options = arrayOf(
                "Die",
                "Don't Die."
            )
        )
    ),
    Pair("38211111",
        Event(
            story = "You might be about to die, that's not good.",
            options = arrayOf(
                "Die",
                "Don't Die."
            )
        )
    ),
    Pair("382111111",
        Event(
            story = "You might be about to die, that's NOT good.",
            options = arrayOf(
                "Die",
                "Don't Die."
            )
        )
    ),
    Pair("3821111111",
        Event(
            story = "You might be about to die, that's NOT good.",
            options = arrayOf(
                "Die",
                "Don't Die."
            )
        )
    ),
    Pair("3821111111",
        Event(
            story = "You might be about to cease to exist along with the game story itself, that's not good.",
            options = arrayOf(
                "Die",
                "Exist"
            )
        )
    ),
    Pair("38211111112",
        Event(
            story = "You might be about to cease to exist anyways along with the game story itself, that's not good.",
            options = arrayOf(
                "Die",
                "Exist"
            )
        )
    ),
    Pair("382111111122",
        Event(
            story = "You might be about to cease to exist anyways along with the game story itself, that's not good.",
            options = arrayOf(
                "Die",
                "Die"
            )
        )
    ),
    Pair("4",
        Event(
            story = "You must unlock this story by finding the Chad easter egg in story 2.",
            options = arrayOf(
                "Return to Story Selection."
            ),
            type = Types.TextOnly,
            performsAction = true,
            action = {
                path = "" //restarts
            },
            actionTime = 7
        )
    ),
    Pair("5",
        Event(
            story = "Too bad.║║ You have to.",
            options = arrayOf(
                "Return to Story Selection."
            ),
            type = Types.TextOnly,
            performsAction = true,
            action = {
                path = "" //restarts
            },
            actionTime = 7
        )
    )

)

val event: Event?
    get() = try {
        events.getValue(path)
    } catch (e: NoSuchElementException) {
        events.getValue("NotFound")
    }

fun getDoActnFromPath(): Boolean {

    return events.values.find { e: Event -> return@find e == event }?.performsAct
        ?: false

}
fun getActnFromPath(): () -> Unit {

    return events.values.find { e: Event -> return@find e == event }?.actn
        ?: {}

}

fun getActTmFromPath(): Int {

    return events.values.find { e: Event -> return@find e == event }?.actnTm
        ?: 1
}

fun getStoryFromPath(): String {

    return events.values.find { e: Event -> return@find e == event }?.stry
        ?: "Oh my, ║the dialogue for this part of the story seems to be missing or has not been written yet. You can restart the game if you'd like to. Actually those options dont work yet either... Just don't cry."

}

fun getEventFromPath(): Event {

    return Event(story = getStoryFromPath(), options = getOptionsFromPath(), performsAction = getDoActnFromPath(), action = getActnFromPath(), actionTime = getActTmFromPath())

}

fun story(stry: Event, clear: Boolean = true) {
    var doesAction = stry.performsAct
    var action = stry.actn
    var actionTime = stry.actnTm
    var options = stry.optns
    var story = stry.stry
    if(doesAction && actionTime == 0) action()
    if(clear) system("cls")
    else println("\n\n\n\n\n\n\n\n\n\n\n\n")
    if(doesAction && actionTime == 1) action()
    typeln("$story\n \n \n", intrvl = 21, pauseAfter = 1100)
    if(doesAction && actionTime == 2) action()
    typeln("What will you do? \n║")
    if(doesAction && actionTime == 3) action()
    wait(150)
    var optNum = 0
    for (option in options) {
        optNum++
        typeln("$optNum ) ║ $option", pauseTime = 150, pauseAfter = 325)
    }
    if(doesAction && actionTime == 4) action()
    wait(150)
    typeln("\nType the number for your selected response and hit enter.\n", intrvl = 25, pauseAfter = 200)
    if(doesAction && actionTime == 5) action()
    type("\nYour Response: ")
    var w4i = true
    do {
        val input = readln().removePrefix(" ").removeSuffix(" ")
        try {
        when (input.toInt()) {
            in (1..optNum) -> {
                w4i = false
                if(doesAction && actionTime == 6) action()
                path = if(event == events.getValue("NotFound")) {
                    ""
                } else "$path${input.toInt()}"
                if(doesAction && actionTime == 7) action()
                wait(711)
                story(getEventFromPath())
            }
            
            else -> {
                if(doesAction && actionTime == 8) action()
                println("Please input one of the given answer choices(e.g. \"1\" \"2\" or \"3\").")
                wait(400)
                typeln("\nWhat will you do?║\n \n")
                typeln("Type the number for your selected answer and hit enter.\n", intrvl = 20, pauseAfter = 200)
                type("\n\nYour Response: ")
                if(doesAction && actionTime == 9) action()
            }

        }
        } catch (e: NumberFormatException) {
            if(doesAction && actionTime == 8) action()
            println("Please input one of the given answer choices(e.g. \"1\" \"2\" or \"3\").")
            wait(400)
            typeln("\nWhat will you do?║\n \n")
            typeln("Type the number for your selected answer and hit enter.\n", intrvl = 20, pauseAfter = 200)
            type("\n\nYour Response: ")
            if(doesAction && actionTime == 9) action()
        } catch (e: Exception) {
            if(doesAction && actionTime == 8) action()
            println("Please input one of the given answer choices(e.g. \"1\" \"2\" or \"3\" with no other characters). \nYou somehow triggered an unknown error with your response.")
            wait(400)
            typeln("\nWhat will you do?║\n \n")
            typeln("Type the number for your selected answer and hit enter.\n", intrvl = 20, pauseAfter = 200)
            type("\n\nYour Response: ")
            if(doesAction && actionTime == 9) action()
        }
        if(doesAction && actionTime == 10) action()
    } while (w4i)
}

fun getOptionsFromPath(): Array<String> {
    return events.values.find { it: Event -> return@find it == event }?.optns
        ?: arrayOf("Restart game.", "Restart game.", "There are no other options,", "Just restart the game.", "Do it.", "Restart", "Because the choices are missing.")

}

fun main() {
    var playing = false

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
                story(getEventFromPath())
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