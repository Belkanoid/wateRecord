package com.belkanoid.waterecord.presentation.recordList


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.belkanoid.waterecord.databinding.FragmentRecordsListBinding
import com.belkanoid.waterecord.presentation.Application
import com.belkanoid.waterecord.presentation.ViewModelFactory
import com.belkanoid.waterecord.presentation.dialog.RecordDialog
import com.belkanoid.waterecord.presentation.recordList.adapter.RecordListAdapter
import javax.inject.Inject


class RecordsListFragment : Fragment() {
    private lateinit var binding: FragmentRecordsListBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel:RecordListViewModel by viewModels { viewModelFactory }

    private val component by lazy {
        (requireActivity().application as Application).component
    }

    private lateinit var recordAdapter: RecordListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecordsListBinding.inflate(inflater, container, false)
        component.inject(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recordAdapter = RecordListAdapter()
        binding.recordRecyclerView.adapter = recordAdapter

        viewModel.list.observe(viewLifecycleOwner){
            recordAdapter.submitList(it)
        }

        recordAdapter.apply {
            onClickRecordItemListener = {
                RecordDialog.newInstance(it.id, it.value, it.image, it.isHot).show(childFragmentManager, "dialog")
            }
        }
    }


    companion object {
        fun newInstance() = RecordsListFragment()
    }
}