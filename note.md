Để thống nhất mọi người sẽ để màu khi vẽ của các thuật toán như sau:
- INMA: xanh lá cây
- HFLGA: xanh nước biển
- RL-Graph: đỏ
- No-charging: xám
Trục hoành: Number of node

Các dữ liệu của RL-Graph trong file excel chưa được cập nhập chính xác nên mọi người phải vào file txt trong các phần thực nghiệm để cập nhập vào file excel rồi mới vẽ

Các hình mọi người lưu vào file pdf rồi mới cho file pdf đó vào overleaf nhé. Cách xuất file pdf của matplotlib mn có thể tham khảo đoạn code sau:

- with PdfPages(r'/content/drive/MyDrive/figure/charge.pdf') as export_pdf3:
-   plt.xticks(label, label)
-   plt.grid('x', color='0.85',linestyle="--")
-   plt.grid('y', color='0.85',linestyle="--")
-   plt.plot(label, charging, marker = 'D', color = 'red')
-   plt.ylabel("Charge energy (kJ)")
-   plt.xlabel("Trimming factor")
-   export_pdf3.savefig()
-   plt.close()

