package ufrpe.mobile.guiaolinda.GUI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ufrpe.mobile.guiaolinda.DB.LocalLab;
import ufrpe.mobile.guiaolinda.R;
import ufrpe.mobile.guiaolinda.Services.Local;


public class LocalListFragment extends Fragment {
    private RecyclerView mLocalRecyclerView;
    private LocalAdapter mAdapter;

    private String categoria;

    public LocalListFragment(String s) {
        setCategoria(s);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local_list, container, false);

        TextView v = (TextView) view.findViewById(R.id.categoria);
        v.setText(String.format("%s", getNomeCategoria(getCategoria())));

        mLocalRecyclerView = (RecyclerView) view.findViewById(R.id.local_recycler_view);
        mLocalRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }
    private String getNomeCategoria(String categoria){
        String cat;
        switch (categoria) {
            case "gastronomia":
                cat = getString(R.string.txt_gastronomia);
                break;
            case "hospedagem":
                cat = getString(R.string.txt_hotelaria);
                break;
            case "igrejas":
                cat = getString(R.string.txt_igrejas);
                break;
            case "monumentos":
                cat = getString(R.string.txt_monumentos);
                break;
            default:
                cat = getString(R.string.txt_gastronomia);
                break;
        }
        return cat;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        LocalLab localLab = LocalLab.get(getActivity());
        List<Local> locais;

        switch (getCategoria()) {
            case "gastronomia":
                locais = localLab.getGastronomicos();
                break;
            case "hospedagem":
                locais = localLab.getHospedagens();
                break;
            case "igrejas":
                locais = localLab.getIgrejas();
                break;
            case "monumentos":
                locais = localLab.getMonumentos();
                break;
            default:
                locais = localLab.getLocais();
                break;
        }
        if (mAdapter == null) {
            mAdapter = new LocalAdapter(locais);
            mLocalRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    public String getCategoria() { return categoria;}

    private class LocalHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mNomeTextView;
        private TextView mFoneTextView;
        private ImageView mLocalImageView;

        private Local mLocal;

        LocalHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_local, parent, false));
            itemView.setOnClickListener(this);

            mNomeTextView = (TextView) itemView.findViewById(R.id.nome_lugar);
            mFoneTextView = (TextView) itemView.findViewById(R.id.fone_lugar);
            mLocalImageView = (ImageView) itemView.findViewById(R.id.imagem_lugar);
        }

        void bind(Local local){
            mLocal = local;
            mNomeTextView.setText(mLocal.get_nome_local());
            mFoneTextView.setText(mLocal.getTelefone());
            mLocalImageView.setImageResource(R.drawable.ic_solved);
        }

        @Override
        public void onClick(View view) {
            Intent intent = LocalPagerActivity.newIntent(getActivity(), mLocal.getId(), getCategoria());
            startActivity(intent);
        }
    }

    private class LocalAdapter extends RecyclerView.Adapter<LocalHolder> {
        private List<Local> mLocais;

        LocalAdapter(List<Local> locais){
            mLocais = locais;
        }

        @Override
        public LocalHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new LocalHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(LocalHolder holder, int position) {
            Local local = mLocais.get(position);
            holder.bind(local);
        }

        @Override
        public int getItemCount() {
            return mLocais.size();
        }
    }
}
