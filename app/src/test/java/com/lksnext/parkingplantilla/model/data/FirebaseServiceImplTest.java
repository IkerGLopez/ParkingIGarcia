package com.lksnext.parkingplantilla.model.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.lksnext.parkingplantilla.model.domain.Callback;
import com.lksnext.parkingplantilla.model.domain.CallbackBoolean;
import com.lksnext.parkingplantilla.model.domain.Reserva;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FirebaseServiceImplTest {

    @Mock
    private FirebaseAuth mockAuth;
    @Mock
    private FirebaseFirestore mockDb;
    @Mock
    private FirebaseUser mockFirebaseUser;
    @Mock
    private AuthResult mockAuthResult;
    @Mock
    private CollectionReference mockCollection;
    @Mock
    private DocumentReference mockDocument;

    private FirebaseServiceImpl firebaseService;

    @Before
    public void setUp() {
        firebaseService = new FirebaseServiceImpl(mockAuth, mockDb);
    }

    @Test
    public void testGetCurrentUserId_WhenLoggedIn() {
        when(mockAuth.getCurrentUser()).thenReturn(mockFirebaseUser);
        when(mockFirebaseUser.getUid()).thenReturn("12345");

        String uid = firebaseService.getCurrentUserId();

        assertEquals("12345", uid);
    }

    @Test
    public void testGetCurrentUserId_WhenNotLoggedIn() {
        when(mockAuth.getCurrentUser()).thenReturn(null);

        String uid = firebaseService.getCurrentUserId();

        assertNull(uid);
    }

    @Test
    public void testSignOut_CallsFirebaseAuth() {
        firebaseService.signOut();
        verify(mockAuth).signOut();
    }
}
