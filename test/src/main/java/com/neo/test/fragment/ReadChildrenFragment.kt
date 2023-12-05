package com.neo.test.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.neo.test.R
import com.neo.test.databinding.FragmentReadChildrenBinding

class ReadChildrenFragment : Fragment(R.layout.fragment_read_children) {

    private var _binding: FragmentReadChildrenBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentReadChildrenBinding.bind(view)

        binding.recyclerView.adapter = TestAdapter()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}

private class TestAdapter : RecyclerView.Adapter<TestAdapter.Holder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Holder {

        return Holder(
            LayoutInflater.from(parent.context).inflate(
                android.R.layout.simple_list_item_1,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = 2

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.text1.text = "test: $position"
    }

    class Holder(
        private val binding: View
    ) : RecyclerView.ViewHolder(binding) {

        val text1 = itemView.findViewById<TextView>(android.R.id.text1)
    }
}
