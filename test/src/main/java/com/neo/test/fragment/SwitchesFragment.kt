package com.neo.test.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.neo.test.databinding.FragmentSwitchesBinding

class SwitchesFragment : Fragment() {

    private var _binding: FragmentSwitchesBinding? = null
    private val binding get() = checkNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSwitchesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCustomSwitch()
    }

    private fun setupCustomSwitch() = with(binding.customSwitcher) {
        root.setOnClickListener {
            switcher.isChecked = !switcher.isChecked
        }
    }
}