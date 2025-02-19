 JTable table = new JTable(data, columnNames);
        JTableHeader header = table.getTableHeader();
        
        table.setRowHeight(30);
        header.setFont(new Font("Helvetica", Font.BOLD, 18));
        table.setFont(new Font("Helvetica", Font.PLAIN, 18));
        
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        
        table.setFillsViewportHeight(true);
        
        RoundedScrollPane scrollPane = new RoundedScrollPane(table, 10);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        header.setBorder(BorderFactory.createEmptyBorder());
        
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        tablePanel.setLayout(new BorderLayout());
        tablePanel.add(scrollPane, BorderLayout.CENTER);
