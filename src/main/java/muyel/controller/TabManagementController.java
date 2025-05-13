package muyel.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * @Mu Ye Liu - Jan 2025
 * 
 * Represents the controller class that manages the the number of game tabs open. 
 */
@RestController
@RequestMapping("/api/tabManagement")
@CrossOrigin(origins = "http://localhost:8080")
public class TabManagementController {
    
    // Stores the tab id's as well as the latest time based on heartbeat. 
    private final Map<String, Long> activeGameTabs = new ConcurrentHashMap<>();

    // Constant to indicate tabs to discard that haven't received an update within 10 seconds.
    private final long TIMEOUT_MILLIS = 10000; // 10 seconds

    // Register tab based on the tabID, then returns the number of tabs open
    @PostMapping("/{tabId}/register")
    public ResponseEntity<Integer> registerTab(@PathVariable String tabId) {
        cleanupStaleTabs();
        activeGameTabs.put(tabId, System.currentTimeMillis());
        return ResponseEntity.ok(activeGameTabs.size());
    }

    // Deregister tab based on the tabID, then returns the number of tabs open
    @PostMapping("/{tabId}/deregister")
    public ResponseEntity<Integer> deregisterTab(@PathVariable String tabId) {
        activeGameTabs.remove(tabId);
        return ResponseEntity.ok(activeGameTabs.size());
    }

    // Use heartbeat to update tab.
    @PostMapping("/{tabId}/heartbeat")
    public ResponseEntity<Boolean> heartbeat(@PathVariable String tabId) {
        activeGameTabs.put(tabId, System.currentTimeMillis());
        return ResponseEntity.ok(true);
    }

    // Returns the number of game tabs open
    @GetMapping("/numberOfGameTabsOpen")
    public ResponseEntity<Integer> numberOfGameTabsOpen() {
        return ResponseEntity.ok(activeGameTabs.size());
    }

    // Cleanup stale tabs that haven't been managed
    private void cleanupStaleTabs() {
        long currentTime = System.currentTimeMillis();
        activeGameTabs.entrySet().removeIf(entry -> currentTime - entry.getValue() > TIMEOUT_MILLIS);
    }
}
