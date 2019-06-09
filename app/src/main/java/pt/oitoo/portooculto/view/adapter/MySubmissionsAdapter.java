package pt.oitoo.portooculto.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;

import pt.oitoo.portooculto.R;
import pt.oitoo.portooculto.databinding.ItemRecyclerviewMySubmissionsBinding;
import pt.oitoo.portooculto.model.Submission;
import pt.oitoo.portooculto.util.FirebaseUtil;

public class MySubmissionsAdapter extends RecyclerView.Adapter<MySubmissionsAdapter.SubmissionViewHolder> {

    private List<? extends Submission> submissionsList;
    private Context context;

    public MySubmissionsAdapter(Context context) {
        this.context = context;
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
    public MySubmissionsAdapter.SubmissionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ItemRecyclerviewMySubmissionsBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.item_recyclerview_my_submissions,
                        viewGroup, false);
        return new SubmissionViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull MySubmissionsAdapter.SubmissionViewHolder holder, int position) {
        holder.binding.setSubmission(submissionsList.get(position));
        if(submissionsList.get(position).getPhotos() != null && !submissionsList.get(position).getPhotos().isEmpty()){
            Glide.with(holder.binding.imgBuilding.getContext())
                    .load(FirebaseUtil.getPhotoRef(submissionsList.get(position).getPhotos().get(0)))
                    .into(holder.binding.imgBuilding);
        }
        if(!submissionsList.isEmpty() && submissionsList.get(position).getStatus() != null){
            switch (submissionsList.get(position).getStatus()){
                case "Not moderated":
                    holder.binding.txtSubmissionStatus.setBackgroundResource(R.drawable.gray_border_background);
                    holder.binding.txtSubmissionStatus.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.dark_gray, null));
                    break;
                case "Approved":
                    holder.binding.txtSubmissionStatus.setBackgroundResource(R.drawable.green_border_background);
                    holder.binding.txtSubmissionStatus.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.oitoo_green, null));
                    break;
                case "Rejected":
                    holder.binding.txtSubmissionStatus.setBackgroundResource(R.drawable.red_border_background);
                    holder.binding.txtSubmissionStatus.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.oitoo_red, null));
                    break;
                default:
                    holder.binding.txtSubmissionStatus.setBackgroundResource(R.drawable.gray_border_background);
                    holder.binding.txtSubmissionStatus.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.dark_gray, null));
                    break;

            }
        }

        holder.binding.executePendingBindings();

    }

    @Override
    public int getItemCount() {
        return submissionsList == null ? 0 : submissionsList.size();
    }

    public class SubmissionViewHolder extends RecyclerView.ViewHolder {

        final ItemRecyclerviewMySubmissionsBinding binding;

        public SubmissionViewHolder(ItemRecyclerviewMySubmissionsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
