package com.rrja.carja.fragment.homemaintenance;

import com.rrja.carja.model.maintenance.MaintenanceService;

/**
   * Created by chongge on 15/8/23.
   */
 public interface TagServiceActionListener {

    public void requestService(String serviceId);

    public void onServiceClicked(MaintenanceService service);
 }
