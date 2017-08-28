package com.jeno.copyyourroom

import org.apache.commons.io.FileUtils
import java.io.File

fun main(args: Array<String>) {
    var targetDirectory = File(args.getOrElse(0, { "" } ))
    println(targetDirectory.absolutePath + " " + targetDirectory.isDirectory)
    targetDirectory.walkTopDown()
            .forEach { println(it.name) }
//    FileUtils.copyDirectory(targetDirectory, )
}