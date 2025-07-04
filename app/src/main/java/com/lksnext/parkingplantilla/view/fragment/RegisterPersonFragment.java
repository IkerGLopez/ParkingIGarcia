//package com.lksnext.parkingplantilla.view.fragment;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.ViewModelProvider;
//
//import com.lksnext.parkingplantilla.R;
////import com.lksnext.parkingplantilla.databinding.FragmentRegisterPersonBinding;
//import com.lksnext.parkingplantilla.viewmodel.RegisterViewModel;
//
//public class RegisterPersonFragment extends Fragment {
//
////    private FragmentRegisterPersonBinding binding;
//    private RegisterViewModel registerViewModel;
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        binding = FragmentRegisterPersonBinding.inflate(inflater, container, false);
//        return binding.getRoot();
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        registerViewModel = new ViewModelProvider(requireActivity()).get(RegisterViewModel.class);
//
//        binding.btnContinuar.setOnClickListener(v -> {
//            String nombre = binding.nombreText.getText().toString();
//            String apellidos = binding.apellidosText.getText().toString();
//            String dni = binding.DNIText.getText().toString();
//            String email = binding.emailText.getText().toString();
//            String password = binding.passwordText.getText().toString();
//            String password2 = binding.password2Text.getText().toString();
//
//        });
//
//        registerViewModel.navigateToRegisterCar().observe(getViewLifecycleOwner(), navigate -> {
//            if (navigate != null && navigate) {
//                requireActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(((ViewGroup) getView().getParent()).getId(), new RegisterCarFragment())
//                        .addToBackStack(null)
//                        .commit();
//                // Opcional: Resetea el LiveData para evitar navegaciones múltiples si el fragment se recrea
//                registerViewModel.onNavigationComplete();
//            }
//        });
//    }
//}
//
//
