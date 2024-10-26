use actix_web::{App, HttpResponse, HttpServer, post, Responder};
use actix_web::web::Path;

#[post("/do-some-action/{id}")]
async fn post_test(id: Path<i32>) -> impl Responder {
    println!("Request do-some-action for {}", id);
    HttpResponse::Ok().finish()
}

#[actix_web::main]
async fn main() -> std::io::Result<()> {
    HttpServer::new(|| {
        App::new()
            .service(post_test)
    })
        .bind(("127.0.0.1", 7070))?
        .run()
        .await
}
