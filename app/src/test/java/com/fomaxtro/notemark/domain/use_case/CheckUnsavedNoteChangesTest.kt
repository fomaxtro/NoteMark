package com.fomaxtro.notemark.domain.use_case

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.fomaxtro.notemark.domain.model.Note
import com.fomaxtro.notemark.domain.model.UnsavedNoteAction
import com.fomaxtro.notemark.util.NoteFactory
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class CheckUnsavedNoteChangesTest {
    companion object {
        @JvmStatic
        fun noteTestCases() = listOf(
            Arguments.of(
                "default",
                "default",
                "",
                NoteFactory.create(
                    title = "default",
                    content = ""
                ),
                UnsavedNoteAction.DELETE
            ),
            Arguments.of(
                "default",
                "Title",
                "",
                NoteFactory.create(
                    title = "default",
                    content = ""
                ),
                UnsavedNoteAction.DISCARD
            ),
            Arguments.of(
                "default",
                "default",
                "New content",
                NoteFactory.create(
                    title = "default",
                    content = ""
                ),
                UnsavedNoteAction.DISCARD
            ),
            Arguments.of(
                "default",
                "Title",
                "Content",
                NoteFactory.create(
                    title = "Title",
                    content = "Content"
                ),
                UnsavedNoteAction.KEEP_SAVED
            )
        )
    }

    @ParameterizedTest
    @MethodSource("noteTestCases")
    fun `Test unsaved note changes`(
        defaultTitle: String,
        currentTitle: String,
        currentContent: String,
        originalNote: Note,
        expectedAction: UnsavedNoteAction
    ) {
        val unsavedNoteAction = CheckUnsavedNoteChanges().invoke(
            defaultTitle = defaultTitle,
            currentTitle = currentTitle,
            currentContent = currentContent,
            originalNote = originalNote
        )

        assertThat(unsavedNoteAction).isEqualTo(expectedAction)
    }
}