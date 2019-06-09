package pt.oitoo.portooculto.view.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import pt.oitoo.portooculto.R;
import pt.oitoo.portooculto.databinding.ItemRecyclerviewBuildingsBinding;
import pt.oitoo.portooculto.model.Submission;
import pt.oitoo.portooculto.util.FirebaseUtil;
import pt.oitoo.portooculto.view.callback.OnSubmissionSelectedListener;

public class SubmissionsAdapter extends RecyclerView.Adapter<SubmissionsAdapter.SubmissionViewHolder> {

    private List<? extends Submission> submissionsList;
    RequestOptions options;

    @Nullable
    private final OnSubmissionSelectedListener onSubmissionSelectedListener;


    public SubmissionsAdapter(OnSubmissionSelectedListener onSubmissionSelectedListener) {
        this.onSubmissionSelectedListener = onSubmissionSelectedListener;
        options = new RequestOptions();
        options.centerCrop();

    }

    public void setSubmissionsList(final List<? extends Submission> items) {
        if (submissionsList == null) {
            submissionsList = items;
            notifyItemRangeInserted(0, items.size());
        } else {
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return submissionsList.size();
                }

                @Override
                public int getNewListSize() {
                    return items.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    Submission oldBuilding = submissionsList.get(oldItemPosition);
                    Submission newBuilding = items.get(newItemPosition);
                    return oldBuilding.getUid() == newBuilding.getUid();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Submission oldBuilding = submissionsList.get(oldItemPosition);
                    Submission newBuilding = items.get(newItemPosition);
                    return oldBuilding.getUid() == newBuilding.getUid();
                }
            });
            submissionsList = items;
            diffResult.dispatchUpdatesTo(this);
        }
    }


    @NonNull
    @Override
    public SubmissionsAdapter.SubmissionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ItemRecyclerviewBuildingsBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.item_recyclerview_buildings,
                        viewGroup, false);
        binding.setCallback(onSubmissionSelectedListener);
        return new SubmissionViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull SubmissionsAdapter.SubmissionViewHolder holder, int position) {
        holder.binding.setSubmission(submissionsList.get(position));
        if(submissionsList.get(position).getPhotos() != null && !submissionsList.get(position).getPhotos().isEmpty()){
            Glide.with(holder.binding.imgBuilding.getContext())
                    .load(FirebaseUtil.getPhotoRef(submissionsList.get(position).getPhotos().get(0)))
                    .apply(options)
                    .into(holder.binding.imgBuilding);
        }
        holder.binding.executePendingBindings();

    }

    @Override
    public int getItemCount() {
        return submissionsList == null ? 0 : submissionsList.size();
    }

    public class SubmissionViewHolder extends RecyclerView.ViewHolder {

        final ItemRecyclerviewBuildingsBinding binding;

        public SubmissionViewHolder(ItemRecyclerviewBuildingsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}