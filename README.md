# Snap-Recycler
Allows views to snap into place after scrolling

#How to install
```
compile 'com.futurethought:snap-recycler:1.0'
```

#How to use
```
SnapRecyclerView snapRecyclerView = (SnapRecyclerView)findViewById(R.id.snapRecyclerView);
snapRecyclerView.setAdapter(yourAdapter);
```

If you want the recyclerview to also auto size your views, use the following
```
snapRecyclerView.setAutoSizeColumns(new AutoSizeColumns.Builder()
                .setVisibleItemCount(3)
                .setOrientation(AutoSizeColumns.HORIZONTAL)
                .setPaddingLeft(25)
                .setPaddiingRight(25)
                .setPercentToShowOfOffViews(.1f)
                .build());
```
