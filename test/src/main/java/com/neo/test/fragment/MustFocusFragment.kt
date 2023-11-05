package com.neo.test.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.neo.test.databinding.FragmentMustFocusBinding

class MustFocusFragment : Fragment() {

    private var _binding: FragmentMustFocusBinding? = null
    private val binding get() = checkNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMustFocusBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }
}