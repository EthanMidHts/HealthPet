package com.ethanchris.android.healthpet.viewmodels;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Goal;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.request.GoalsReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class GoogleFitViewModel extends ViewModel {
    public void readLastDayStepCounts(Context context, GoogleSignInAccount account, OnCompleteListener<DataReadResponse> callback) {
        Task<DataReadResponse> response = Fitness.getHistoryClient(context, account)
                .readData(new DataReadRequest.Builder()
                        .read(DataType.AGGREGATE_STEP_COUNT_DELTA)
                        .setTimeRange(System.currentTimeMillis() - (24*60*60*1000), System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                        .build());
        response.addOnCompleteListener(callback);
    }

    public void readGoals(Context context, GoogleSignInAccount account, OnCompleteListener<List<Goal>> callback) {
        Task<List<Goal>> goalResponse = Fitness.getGoalsClient(context, account)
                .readCurrentGoals(new GoalsReadRequest.Builder()
                        .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA)
                        .build());
        goalResponse.addOnCompleteListener(callback);
    }
}
