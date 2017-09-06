package com.jeno.copyyourroom

import javafx.application.Application
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.text.FontWeight
import javafx.stage.DirectoryChooser
import org.apache.commons.io.FileUtils
import org.apache.commons.io.filefilter.DirectoryFileFilter
import tornadofx.*
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths


fun main(args: Array<String>) {
    Application.launch(CopyStructureApp::class.java)
}

class CopyStructureApp : App(CopyStructureView::class, Styles::class)

class Styles : Stylesheet() {
    init {
        label {
            fontSize = 20.px
            fontWeight = FontWeight.BOLD
            backgroundColor += c("#cecece")
        }
    }
}

class CopyStructureView : View() {

    override val root: HBox by fxml("/CopyStructureView.fxml")
    val sourceDirectoryChooser: Button by fxid()
    val inputFolderLabel: Label by fxid()

    val targetDirectoryChooser: Button by fxid()
    val outputFolderLabel: Label by fxid()

    val copyButton: Button by fxid()
    val msgBox: Label by fxid()

    init {
        initDirectoryChooser("Choose source directory", sourceDirectoryChooser, inputFolderLabel)
        initDirectoryChooser("Choose target directory", targetDirectoryChooser, outputFolderLabel)
        copyButton.text = "Copy"
        copyButton.onAction = EventHandler {
            val source = File(inputFolderLabel.text)
            val target = File(outputFolderLabel.text)
            val sourceDirectoryExists = directoryExists(source)
            val targetDirectoryExists = directoryExists(target)
            if (sourceDirectoryExists && targetDirectoryExists) {
                copyDirectoryWithZeroLengthFiles(source, target)
                msgBox.addClass("valid")
                msgBox.text = "Copy finished"
            } else {
                // Error
                msgBox.addClass("invalid")
                if (!sourceDirectoryExists) {
                    msgBox.text = getDirectoryError(true, inputFolderLabel.text)
                } else {
                    msgBox.text = getDirectoryError(false, inputFolderLabel.text)
                }
            }

        }
    }

    private fun getDirectoryError(sourceOrTarget: Boolean, directory: String) =
            "The choosen " + if (sourceOrTarget) "source" else "target" + " (" + directory + ") directory either doesn't exist or is not a directory"

    private fun directoryExists(source: File) = source.exists() && source.isDirectory

    private fun initDirectoryChooser(buttonText: String, chooser: Button, pathTextField: Label) {
        chooser.text = buttonText
        chooser.onAction = EventHandler {
            var directoryChooser = DirectoryChooser()
            val inputFolder = directoryChooser.showDialog(currentStage)

            if (inputFolder != null) {
                pathTextField.setText(inputFolder.path)
            }
        }
    }

    @Throws(IOException::class)
    private fun copyDirectoryWithZeroLengthFiles(source: File, target: File) {
        FileUtils.copyDirectory(source, target, DirectoryFileFilter.DIRECTORY)

        val fileTreeStream = Files.walk(Paths.get(source.toURI()))
        fileTreeStream
                .map { it.toFile() }
                .filter { it.isFile() }
                .forEach { f ->
                    val fileToCreate = File(target.absolutePath + f.parentFile.absolutePath.replace(source.absolutePath, "") + "/" + f.name)
                    if (!fileToCreate.exists()) {
                        try {
                            fileToCreate.createNewFile()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                    }
                }
    }

}
