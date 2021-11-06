package com.example.flows

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.flows.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btLivedata.setOnClickListener {
            viewModel.triggerLiveData()
        }

        binding.btStateflow.setOnClickListener {
            viewModel.triggerStateFlow()
        }

        //on the normal flow cannot observe, because it don't have any state, when the screen rotated, the flow will be destory
        //flow it's just execute one time then release the memory that flow hold.
        binding.btFlow.setOnClickListener {
            lifecycleScope.launch {
                viewModel.triggerFlow().collectLatest {
                    binding.tvFlow.text = it
                }
            }
        }

        binding.btSharedflow.setOnClickListener {
            viewModel.triggerSharedFlow()
        }

        subscribeToObservables()
    }

    private fun subscribeToObservables() {
        //LiveData observe
        viewModel.liveData.observe(this) {
            binding.tvLivedata.text = it
        }

        //StateFlow observe (stateflow is base on kotlin coroutine, should be launch coroutines)
        //compare to LiveData, stateflow its much easier to test
        /*lifecycleScope.launchWhenCreated {
            viewModel.stateFlow.collectLatest {
                binding.tvStateflow.text = it
            }
        }*/

        //Compare StateFlow and SharedFlow
        //StateFlow: when screen rotated goto onPause and after onResume
        //no matter you rotate how many times, it will show again and again when screen back to onCreated.
        lifecycleScope.launchWhenCreated {
            viewModel.stateFlow.collectLatest {
                Snackbar.make(
                    binding.root,
                    it,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        //Shared Flow only can just show one time if the data doesn't change
        lifecycleScope.launch {
            viewModel.sharedFlow.collectLatest {
                Snackbar.make(
                    binding.root,
                    it,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }


}