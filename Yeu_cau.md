Đề tài 1 (MySSH)
Viết ứng dụng myssh mô phỏng chương trình “ssh” để Client kết nối từ xa đến Server thực hiện các lệnh hệ thống trên Server. Yêu cầu:
- Dùng socket, giao thức TCP.
- Giao diện dòng lệnh
- Multiclients: cho phép nhiều clients kết nối đồng thời (giới hạn N clients, vượt quá N thì không cho phép kết nối)
- Khi client kết nối tới phải đăng nhập (username / pass). CSDL người dùng là của chương trình, không phải của hệ thống, cơ chế lưu tùy ý (CSDL, text, v.v)
+ Server thông báo kết nối thành công hay không
- Khi kết nối thành công thì client có thể thực hiện các lệnh trên hệ thống của server
+ Các lệnh có thể có tham số hoặc không
+ Một số lệnh cơ bản: hiển thị thư mực hiện thời, in danh sách các tệp, thư mục trong thư mục hiện thời, xóa, di chuyển tệp, thư mục, hiển thị ngày giờ hệ thống, v.v
- Server phân tích lệnh nhận được từ phía client, thực hiện và trả kết quả cho client
+ Nếu các lệnh thực hiện không được phép chạy thì phải thông báo lỗi.
+ Lệnh chạy trên server phụ thuộc vào hệ điều hành mà server được cài đặt lên (Windows, Linux)
- Kết thúc phiên làm việc client gửi lệnh ngắt kết nối tới server và chấm dứt kết nối.
- Yêu cầu đặc biệt: Có lệnh get cho phép tải về 1 tệp dạng text từ Server về Client (nếu tệp lớn, có thể ngắt nhỏ ra để gửi).
Đề tài 2 (MyInternetBanking)
Tạo một hệ thống ngân hàng phân tán đơn giản. Hệ thống này sẽ bao gồm hai chương trình đóng vai trò như các dịch vụ. Dịch vụ thứ nhất đại diện cho ngân hàng. Ngân hàng này có một số tài khoản (account) của khách hàng. Các tài khoản có thể được tạo ra cho khách hàng (với giá trị tiền gửi ban đầu bất kì không âm) và có thể được hủy. Ngoài ra, các tài khoản có thể được cộng thêm tiền gửi vào. Với là số dư tài khoản là dương, người sử dụng có thể rút tiền ra.
Dịch vụ đại diện khách hàng cung cấp quyền truy cập vào các chức năng của ngân hàng như các truy vấn số dư tiền gửi, cộng thêm tiền gửi vào và rút tiền ra.
Chú ý lỗi sẽ xảy ra nếu ngân hàng cố gắng tạo một tài khoản đã tồn tại hoặc hủy một tài khoản không tồn tại. Hệ thống này sẽ giống như một ngân hàng trong thực tế, sẽ không rút được tiền nếu không có đủ tiền trong tài khoản.
Hệ thống sử dụng cơ chế truyền thông socket giữa máy khách (khách hàng) và ngân hàng (server).
Lỗi được hiển thị trong chương trình của phía khách hàng bằng một cách nào đó (ví dụ, chương trình sẽ chỉ dẫn cho người dùng tại sao một hoạt động không thể được).
Đề tài 3 (MyGameServer)
Viết 1 game server (GS) đơn giản. Yêu cầu:
- Dùng webservices, tùy chọn REST hay SOAP
- GS cung cấp các dịch vụ sau:
+ Đăng ký người chơi, 1 nickname có thể được dùng cho nhiều trò chơi khác nhau (đây là game server tổng quát)
+ Cập nhật thứ hạng (rank) người chơi theo điểm đạt được trong 1 trò chơi nào đó
+ Xem thứ hạng của tất cả người chơi (sắp xếp), của 1 người chơi nào đó theo từng trò chơi
+ Tạo phòng chơi (room) cho 1 trò chơi,
+ Xem danh sách các phòng chơi của 1 trò chơi
+ Cho phép người chơi gia nhập 1 room, số lượng người chơi trong 1 room được giới hạn. Khi room đã đầy thì thông báo lại khi có người muốn gia nhập. 
+ Cho phép người chơi thoát khỏi room
+ Trả về danh sách phòng chơi đang có trong hệ thống (phòng chơi không có ai kết nối sẽ bị xóa lập tức), số người chơi trong phòng chơi
+ Cho điểm người trong trong 1 phòng chơi sau khi trò chơi kết thúc.
+ Sử dụng CSDL (tùy chọn) để quản lý người chơi, phòng chơi, danh sách trò chơi
+ Cần có chương trình test lại tất cả các dịch vụ của GS ở trên.
Đề tài 4 (MyStorage)
Viết ứng dụng phân tán đơn giản chia sẻ và đồng bộ hóa dữ liệu. Yêu cầu:
- Dữ liệu được phía client đưa lên máy Server 
- Server thực hiện việc đồng bộ hóa tự động giữa các máy khách (Clients) dựa trên 2 yếu tố đồng thời: 
+ kích thước của file dữ liệu 
+ thời gian thay đổi file dữ liệu.
- Client tự động upload và download dữ liệu
- Cung cấp giao diện đơn giản (dạng Explorer) để hiển thị dữ liệu dạng phía Client 
- Cung cấp giao diện đơn giản (có thể là CLI) để hiển thị dữ liệu phía Server
- Cho phép người sử dụng thoát ứng dụng tự động đồng bộ phía Client.
