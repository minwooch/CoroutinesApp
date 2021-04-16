package com.applsh.coroutinesapp.ui.some

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.addRepeatingJob
import com.applsh.coroutinesapp.R
import com.applsh.coroutinesapp.databinding.SomeFragmentBinding
import com.applsh.coroutinesapp.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SomeFragment : BaseFragment<SomeFragmentBinding>() {

    override val layout: Int
        get() = R.layout.some_fragment

    private val someViewModel: SomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.someViewModel = someViewModel

        viewLifecycleOwner.addRepeatingJob(Lifecycle.State.STARTED) {
            launch {
                someViewModel.dataStateOutput.collect { // 상태를 받아 UI를 업데이트
                    if (it != null)
                        binding.dataStateTextView.text = "state $it"
                }
            }
            launch {
                someViewModel.dataEventOutput.collect { // 이벤트를 받아 로그를 찍음
                    Log.v("SomeFragment", "event log $it")
                }
            }
            launch {
                someViewModel.loadingState.collect { // 로딩 상태를 받아 UI를 업데이트
                    binding.loadingTextView.text = if (it) "loading" else "not loading"
                }
            }
        }

    }
}