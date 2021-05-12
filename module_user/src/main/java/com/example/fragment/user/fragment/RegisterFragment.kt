package com.example.fragment.user.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.example.fragment.library.common.constant.NavMode
import com.example.fragment.library.common.constant.Router
import com.example.fragment.library.common.fragment.ViewModelFragment
import com.example.fragment.library.common.utils.UserInfoManager
import com.example.fragment.module.user.databinding.FragmentRegisterBinding
import com.example.fragment.user.model.UserModel


class RegisterFragment : ViewModelFragment<FragmentRegisterBinding, UserModel>() {

    override fun setViewBinding(inflater: LayoutInflater): FragmentRegisterBinding {
        return FragmentRegisterBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        update()
    }

    private fun setupView() {
        binding.username.addKeyboardListener(binding.root)
        binding.password.addKeyboardListener(binding.root)
        binding.repassword.addKeyboardListener(binding.root)
        binding.login.setOnClickListener {
            baseActivity.onBackPressed()
        }
        binding.register.setOnClickListener {
            val username = binding.username.text.toString()
            val password = binding.password.text.toString()
            val repassword = binding.repassword.text.toString()
            if (checkParameter(username, password, repassword)) {
                viewModel.register(username, password, repassword)
            }
        }
    }

    private fun update() {
        viewModel.registerResult.observe(viewLifecycleOwner, {
            if (it.errorCode == "0") {
                it.data?.apply {
                    UserInfoManager.setUser(this)
                }
                baseActivity.navigation(Router.MAIN, navMode = NavMode.POP_BACK_STACK)
            } else {
                baseActivity.showTips(it.errorMsg)
            }
        })
    }

    private fun checkParameter(username: String, password: String, repassword: String): Boolean {
        if (username.isEmpty()) {
            baseActivity.showTips("用户名不能为空")
            return false
        }
        if (password.isEmpty()) {
            baseActivity.showTips("密码不能为空")
            return false
        }
        if (repassword.isEmpty()) {
            baseActivity.showTips("确认密码不能为空")
            return false
        }
        if (password != repassword) {
            baseActivity.showTips("两次密码不一样")
            return false
        }
        return true
    }

}