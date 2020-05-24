package com.ror.weirddatainput

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.afollestad.materialdialogs.MaterialDialog
import com.ror.weirddatainput.extensions.toast
import com.ror.weirddatainput.repositories.LoadableStatus
import kotlinx.android.synthetic.main.fragment_sample.*
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class SampleFragment : Fragment(R.layout.fragment_sample) {
    private val viewModel: SampleFragmentViewModel by stateViewModel()

    private lateinit var backPressedCallback: OnBackPressedCallback

    private val taxChangeWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            //does nothing
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            viewModel.reactToInput(field1Input.text.toString(), field2Input.text.toString())
        }

        override fun afterTextChanged(s: Editable?) {
            //does nothing
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        backPressedCallback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            MaterialDialog(requireContext()).apply {
                title(R.string.app_modified_title)
                message(R.string.app_modified_message)
                positiveButton(R.string.app_save) {
                    save()
                }
                negativeButton(R.string.app_cancel) {
                    justExit()
                }
                show()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()

        viewModel.dummyData.observe(viewLifecycleOwner, Observer {
            val isLoading = it.status == LoadableStatus.LOADING

            loadingProgress.visibility = if (isLoading) View.VISIBLE else View.GONE
            field1Input.isEnabled = !isLoading
            field2Input.isEnabled = !isLoading

            if (it.status == LoadableStatus.SUCCESS) {
                setValueSilently(field1Input, it.data!!.foo1)
                setValueSilently(field2Input, it.data.foo2)
            } else if (it.status == LoadableStatus.ERROR) {
                when (it.exception) {
                    is RequiredField1 -> {
                        field1Input.error = getString(R.string.app_validation_required_field)
                    }
                    is RequiredField2 -> {
                        field2Input.error = getString(R.string.app_validation_required_field)
                    }
                    else -> {
                        toast(R.string.is_action_load_failed)
                    }
                }
            }
        })

        viewModel.saveable.observe(viewLifecycleOwner, Observer {
            save.isEnabled = it
        })

        viewModel.modified.observe(viewLifecycleOwner, Observer {
            backPressedCallback.isEnabled = it
        })
    }

    private fun save() {
        toast(R.string.is_action_save_imitation)
    }

    private fun justExit() {
        requireActivity().finish()
    }

    private fun setValueSilently(editText: EditText, value: String) {
        editText.removeTextChangedListener(taxChangeWatcher)

        if (editText.text.toString() != value) {
            editText.setText(value)
            editText.setSelection(value.length)
            editText.error = null
        }

        editText.addTextChangedListener(taxChangeWatcher)
    }

    private fun setupListeners() {
        field1Input.addTextChangedListener(taxChangeWatcher)
        field2Input.addTextChangedListener(taxChangeWatcher)
        save.setOnClickListener { toast(R.string.is_action_save_imitation) }
    }
}
