pipeline {
    agent any
    tools {
            // Gọi chính xác cái tên bạn vừa đặt ở Bước 1
            maven 'Maven 3.9.6'
        }
    // BƯỚC QUAN TRỌNG: Tự động lên GitHub kiểm tra code mới mỗi 2 phút
    // H/2 nghĩa là Hash/2 (khoảng 2 phút 1 lần). Không cần Webhook hay Ngrok!
    triggers {
        pollSCM('H/2 * * * *')
    }

    stages {
        stage('1. Kéo Source Code') {
            steps {
                echo 'Đang tải code mới nhất từ nhánh main...'
                // Lệnh này bảo Jenkins lấy code từ repo đã cấu hình trong Job
                checkout scm
            }
        }

        stage('2. Biên dịch (Compile)') {
            steps {
                echo 'Đang kiểm tra cú pháp và biên dịch mã nguồn...'
                // Dùng lệnh 'bat' cho máy tính Windows
                bat 'mvn clean compile'
            }
        }

        stage('3. Chạy Toàn bộ Test') {
            steps {
                echo 'Đang kích hoạt Unit Test & Integration Test...'
                // Chạy tất cả các class kết thúc bằng chữ "Test"
                bat 'mvn test'
            }
        }

        stage('4. Đóng gói (Package)') {
            steps {
                echo 'Test xanh mượt! Đang đóng gói thành file JAR...'
                // Thêm -DskipTests để không phải chạy lại test lần 2 cho tốn thời gian
                bat 'mvn package -DskipTests'
            }
        }
    }

    post {
        always {
            echo 'Đang tổng hợp báo cáo Test...'
            // Thu thập các file XML chứa kết quả test để vẽ biểu đồ trên Jenkins
            junit 'target/surefire-reports/*.xml'
        }
        success {
            echo '✅ TUYỆT VỜI! Pipeline chạy thành công! Code của bạn đã an toàn.'
        }
        failure {
            echo '❌ OOPS! Có gì đó sai sai. Hãy bấm vào Console Output để xem lỗi ở đâu.'
        }
    }
}