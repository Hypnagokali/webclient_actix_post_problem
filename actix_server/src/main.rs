use actix_web::{App, HttpResponse, HttpServer, post, Responder};
use actix_web::web::Bytes;

#[post("/do-some-action")]
async fn post_test() -> impl Responder {
    println!("Request do-some-action");
    HttpResponse::Ok().finish()
}

#[post("/do-some-action-read-body")]
async fn post_test_read_body(body: Bytes) -> impl Responder {
    println!("Request do-some-action-read-body");
    println!("Body length = {}", body.len()); // this is not needed.
    HttpResponse::Ok().finish()
}

#[actix_web::main]
async fn main() -> std::io::Result<()> {
    HttpServer::new(|| {
        App::new()
            .service(post_test)
            .service(post_test_read_body)
    })
        .bind(("127.0.0.1", 7070))?
        .run()
        .await
}
